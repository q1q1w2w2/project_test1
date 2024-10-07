package com.example.demo1.service;

import com.example.demo1.domain.Orders;
import com.example.demo1.domain.Review;
import com.example.demo1.domain.User;
import com.example.demo1.dto.item.CreateReviewDto;
import com.example.demo1.dto.item.ReviewSearch;
import com.example.demo1.repository.OrdersRepository;
import com.example.demo1.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrdersRepository ordersRepository;

    @Transactional
    public Review saveReview(CreateReviewDto dto, Long userId) {

        Orders orders = ordersRepository.findById(dto.getOrdersIdx())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주문입니다."));

        if (!orders.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 주문을 생성한 유저가 아닙니다.");
        }

        if (orders.getStep() != 3) {
            throw new RuntimeException("배송이 완료된 이후 리뷰를 달아주세요.");
        }

        Review review = Review.builder()
                .orders(orders)
                .review(dto.getReview())
                .score(dto.getScore())
                .createAt(LocalDateTime.now().withNano(0))
                .updateAt(LocalDateTime.now().withNano(0))
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> getReview(ReviewSearch reviewSearch) {
        return reviewRepository.findAll(reviewSearch);
    }
}
