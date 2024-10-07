package com.example.demo1.dto.item;

import com.example.demo1.domain.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {

    private Long orderIdx;

    private String review;

    private int score;

    public ReviewResponseDto(Review review) {
        this.orderIdx = review.getOrders().getIdx();
        this.review = review.getReview();
        this.score = review.getScore();
    }
}
