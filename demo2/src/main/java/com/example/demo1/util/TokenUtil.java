package com.example.demo1.util;

import com.example.demo1.domain.User;
import com.example.demo1.jwt.TokenProvider;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtil {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    private String extractToken(String authorization) {
        return authorization.substring(7);
    }

    public User extractUserByToken(String authorization) throws Exception {
        String token = extractToken(authorization);
        String userId = tokenProvider.extractUserIdFromRefreshToken(token);
        return userService.findByLoginId(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }
}
