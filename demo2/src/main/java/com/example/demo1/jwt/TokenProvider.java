package com.example.demo1.jwt;

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
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {

    private static final String AUTHORIZATION_KEY = "auth";

    private SecretKey key;
    private SecretKey claimKey;

    private final String secret;
    private final long tokenValidityInMilliseconds;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds,
            @Value("${jwt.claim-key}") String claimKeyString
    ) throws Exception {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
        this.claimKey = AesUtil.generateKeyFromString(claimKeyString);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] decode = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decode);
    }

    public String createToken(Authentication authentication) throws Exception {

        String subject = authentication.getName();
        String authority = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(""); // 권한이 없을 경우 빈 문자열

        long now = new Date().getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);

        String encryptSubject = AesUtil.encrypt(subject, claimKey);
        String encryptAuthority = AesUtil.encrypt(authority, claimKey);

        return Jwts.builder()
                .subject(encryptSubject)
                .claim("authority", encryptAuthority)
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
                .orElse("");
        String authority = AesUtil.decrypt(encryptAuthority, claimKey);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authority));

        User principal = new User(subject, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
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
