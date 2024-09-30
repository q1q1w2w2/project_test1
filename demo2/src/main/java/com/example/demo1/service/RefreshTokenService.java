package com.example.demo1.service;

import com.example.demo1.domain.RefreshToken;
import com.example.demo1.domain.User;
import com.example.demo1.exception.TokenValidationException;
import com.example.demo1.repository.RefreshTokenRepository;
import com.example.demo1.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveRefreshToken(String refreshToken, String userId) {
        Optional<User> user = userRepository.findByLoginId(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user.get())
                .createdAt(LocalDateTime.now().withNano(0))
                .expiredAt(null)
                .build());
    }

    @Transactional
    public void updateRefreshToken(String refreshToken) {
        Optional<RefreshToken> byRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (byRefreshToken.isEmpty()) {
            throw new TokenValidationException("토큰이 없습니다.");
        }
        byRefreshToken.get().updateRefreshToken();
    }

    public boolean isRefreshTokenExpired(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenValidationException("토큰이 없습니다."));
        return token.getExpiredAt() != null;
    }

}
