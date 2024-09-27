package com.example.demo1.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // jwt 인증 정보를 Security Context 에 저장하는 필터
        log.info("JWT 인증 정보 Security Context에 저장");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();
        log.info("jwt: {}", jwt);

        // 토큰 유효성 확인
        // 여기서 refresh token 유효성 확인 후 새로운 액세스 토큰 발행
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = null;
            try {
                authentication = tokenProvider.getAuthentication(jwt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보 저장, uri: {}", authentication.getName(), requestURI);
        } else {
            log.info("유효한 JWT 없음, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    // request Header 에서 토큰 정보 꺼냄
    public String resolveToken(HttpServletRequest request) {
        log.info("header에서 토큰 꺼냄");
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
