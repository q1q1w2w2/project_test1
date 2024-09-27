package com.example.demo1.jwt;

import com.example.demo1.service.CustomUserDetailsService;
import com.example.demo1.util.AesUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {

    private static final String AUTHORIZATION_KEY = "auth";

    private SecretKey key;
    private SecretKey claimKey;

    private final String secret;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
    private final CustomUserDetailsService customUserDetailsService;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expire-time}") long accessTokenExpireTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTokenExpireTime,
            @Value("${jwt.claim-key}") String claimKeyString,
            CustomUserDetailsService customUserDetailsService
    ) throws Exception {
        this.secret = secret;
        this.accessTokenExpireTime = accessTokenExpireTime * 1000;
        this.refreshTokenExpireTime = refreshTokenExpireTime * 1000;
        this.claimKey = AesUtil.generateKeyFromString(claimKeyString);
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] decode = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decode);
    }

    public String createAccessToken(Authentication authentication) throws Exception {
        String subject = authentication.getName();

        String authority = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");
        String encryptSubject = AesUtil.encrypt(subject, claimKey);
        String encryptAuthority = AesUtil.encrypt(authority, claimKey);

        return createToken(encryptSubject, encryptAuthority, accessTokenExpireTime);
    }

    public String createRefreshToken(Authentication authentication) throws Exception {
        String subject = authentication.getName();

        String encryptSubject = AesUtil.encrypt(subject, claimKey);
        String encryptAuthority = AesUtil.encrypt("ROLE_USER", claimKey);

        return createToken(encryptSubject, encryptAuthority, refreshTokenExpireTime);
    }

    public String createNewAccessToken(String refreshToken) throws Exception {
        Authentication authentication = getAuthenticationFromRefreshToken(refreshToken);
        return createAccessToken(authentication);
    }

    private String createToken(String subject, String authority, long expireTime) {
        long now = new Date().getTime();
        Date validity = new Date(now + expireTime);

        return Jwts.builder()
                .subject(subject)
                .claim("authority", authority)
                .issuedAt(new Date())
                .expiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) throws Exception {
        Claims claims = Jwts.parser()
                .verifyWith(key) // secretKey
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String subject = AesUtil.decrypt(claims.getSubject(), claimKey);
        String encryptAuthority = Optional.ofNullable(claims.get("authority"))
                .map(Object::toString)
                .orElse("ROLE_USER");
        String authority = AesUtil.decrypt(encryptAuthority, claimKey);

        return createAuthentication(subject, token, authority);
    }

    public Authentication getAuthenticationFromRefreshToken(String refreshToken) throws Exception {
        String subject = extractUserIdFromRefreshToken(refreshToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(subject);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        String authority = authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        return createAuthentication(subject, refreshToken, authority);
    }

    private Authentication createAuthentication(String subject, String token, String authority) {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(authority));
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authority));

        User principal = new User(subject, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String extractUserIdFromRefreshToken(String refreshToken) throws Exception {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getPayload();
        String encryptSubject = claims.getSubject();
        return AesUtil.decrypt(encryptSubject, claimKey);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


}
