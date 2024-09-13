package com.example.jwt2.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    // application.yml 에서 secretKey, 토큰 유효시간 관리
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
    }

    // afterPropertiesSet() -> InitializingBean 인터페이스의 메서드
    // 빈이 초기화될 때 로직(DB 연결 설정, 필드 초기화 등)
    @Override
    public void afterPropertiesSet() throws Exception {
        // 비밀키 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // HMAC(해시 기반 메시지 인증 코드) SHA 알고리즘에 사용할 수 있는 키 객체 생성
        // JWT 생성 및 검증에 사용됨
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        // 권한 정보 생성
        // 사용자 권한 목록 가져옴 -> 스트림 변환 -> 각 권한 객체에서 권한 문자열 추출
        // -> 권한 문자열을 쉼표 구분하여 하나의 문자열로(ROLE_USER,ROLE_ADMIN)
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 현재 시간 millisecond 로 가져옴
        long now = (new Date()).getTime();
        // 토큰 만료 시간
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // JWT 생성
        // claim -> payload 부분에 담는 정보의 한 조각(name: value의 한 쌍)

        // setSubject: JWT의 subject로 인증된 사용자 이름 설정(payload의 subject)
        // claim: 클레임에 권한 정보 추가
        // signWith: 지정된 비밀키와 HS512 해시 알고리즘으로 서명 (시그니처로 변환?)
        // setExpiration: 만료 시간
        // compact: JWT를 문자열 형태로 반환
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // JWT 검증하고, 그 결과로 사용자의 인증 정보를 포함하는 Authentication 객체를 생성하여 반환
    public Authentication getAuthentication(String token) {
        // parserBuilder: JWT 파싱하고 검증하기 위한 빌더 생성
        // setSigningKey: JWT 서명 시 사용된 비밀 키 설정 -> 이 키로 JWT 서명 검증
        // build: 파서 빌더를 완료하고 JWT 파서 생성
        // parseClaimsJws: 주어진 JWT를 파싱하여 claim정보 추출, 여기서 JWT 유효성 확인
        // getBody: 파싱된 JWT의 payload 부분인 claim을 가져옴
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // JWT에서 권한 정보 추출하여 GrantedAuthority객체의 컬렉션으로 변환
        // claims.get: JWT에서 권한 정보 가져옴(ROLE_USER,ROLE_ADMIN), 문자열 분리하여 배열로 변환
        // 각 권한 문자열을 SimpleGrantedAuthority 객체로 변환
        // 변환된 객체를 리스트로 수집
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // User(security의 User객체)
        // claims.getSubject: JWT에서 설정한 subject(사용자 이름/ID) 가져옴
        // 두 번째 인자는 password이지만, JWT로 인증할 때는 비밀번호가 불필요
        // authorities: 앞에서 생성한 권한 컬렉션 전달
        User principal = new User(claims.getSubject(), "", authorities);

        // 최종적으로 UsernamePasswordAuthenticationToken 객체 반환(Spring Security)
        // (현재 인증된 사용자, JWT 토큰, 사용자의 권한 정보)
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            // JWT가 유효한지 확인
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
