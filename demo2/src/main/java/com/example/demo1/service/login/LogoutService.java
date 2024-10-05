package com.example.demo1.service.login;

import com.example.demo1.exception.TokenValidationException;
import com.example.demo1.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void logout(String refreshToken, String accessToken) {

        if (!tokenProvider.validateToken(accessToken) || !tokenProvider.validateToken(refreshToken)) {
            throw new TokenValidationException("토큰이 유효하지 않습니다.");
        }

        if (refreshTokenService.isRefreshTokenExpired(refreshToken)) {
            throw new TokenValidationException("만료된 토큰입니다.");
        }

        refreshTokenService.updateRefreshToken(refreshToken);

        log.info("로그아웃 성공");
    }
}
