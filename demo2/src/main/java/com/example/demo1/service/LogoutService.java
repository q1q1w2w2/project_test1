package com.example.demo1.service;

import com.example.demo1.domain.BlackList;
import com.example.demo1.exception.TokenValidationException;
import com.example.demo1.jwt.TokenProvider;
import com.example.demo1.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutService {

    private final TokenProvider tokenProvider;
    private final BlackListRepository blackListRepository;

    @Transactional
    public void logout(String refreshToken, String accessToken) {
        if (tokenProvider.validateToken(accessToken) != true || tokenProvider.validateToken(refreshToken) != true) {
            throw new TokenValidationException("토큰이 유효하지 않습니다.");
        }

        blackListRepository.save(new BlackList(refreshToken));
        log.info("로그아웃 성공");
    }
}
