package com.example.demo1.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id @GeneratedValue
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_idx")
    private Orders orders;

    private String review;

    private int score; // 1~5 점

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    public Review(Orders orders, String review, int score, LocalDateTime createAt, LocalDateTime updateAt) {
        this.orders = orders;
        this.review = review;
        this.score = score;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
