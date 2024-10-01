package com.example.demo1.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id @GeneratedValue
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    private String refreshToken;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    @Builder
    public RefreshToken(User user, String refreshToken, LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    public void updateRefreshToken() {
        this.expiredAt = LocalDateTime.now().withNano(0);
    }
}
