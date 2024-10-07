package com.example.demo1.dto.item;

import com.example.demo1.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetReviewListDto {

    private Long ordersIdx;
    private int score;
    private String review;

    public GetReviewListDto(Review review) {
        this.ordersIdx = review.getOrders().getIdx();
        this.score = review.getScore();
        this.review = review.getReview();
    }
}
