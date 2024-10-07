package com.example.demo1.dto.item;

import com.example.demo1.domain.Orders;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class CreateReviewDto {

    private Long ordersIdx;

    private String review;

    @Range(min = 1, max = 5, message = "점수는 1~5점 사이여야 합니다.")
    private int score; // 1~5 점

}
