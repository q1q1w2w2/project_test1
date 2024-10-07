package com.example.demo1.web;

import com.example.demo1.domain.Cart;
import com.example.demo1.domain.Orders;
import com.example.demo1.domain.Review;
import com.example.demo1.domain.User;
import com.example.demo1.dto.item.*;
import com.example.demo1.service.OrderService;
import com.example.demo1.service.ReviewService;
import com.example.demo1.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final TokenUtil tokenUtil;
    private final ReviewService reviewService;

    @PostMapping("/item/cart/add")
    public ResponseEntity<CartResponseDto> addCart(@RequestBody AddCartDto dto, @RequestHeader String authorization) throws Exception {
        User user = tokenUtil.extractUserByToken(authorization);
        Cart cart = orderService.saveCart(dto, user);

        CartResponseDto response = new CartResponseDto(cart);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/item/orders")
    public ResponseEntity<OrderResponseDto> createOrders(@RequestBody CreateOrdersDto dto, @RequestHeader String authorization) throws Exception {
        User user = tokenUtil.extractUserByToken(authorization);
        Orders orders = orderService.saveOrders(user, dto.getItemIdx(), dto.getQuantity());

        OrderResponseDto response = new OrderResponseDto(orders);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/item/orders/review")
    public ResponseEntity<ReviewResponseDto> createReview(@Validated @RequestBody CreateReviewDto dto, @RequestHeader String authorization) throws Exception {
        User user = tokenUtil.extractUserByToken(authorization);
        Review review = reviewService.saveReview(dto, user.getId());

        ReviewResponseDto response = new ReviewResponseDto(review);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/item/orders/update-step")
    public ResponseEntity<String> updateStep(@RequestBody UpdateOrderStepDto dto) {
        orderService.updateStep(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("변경이 완료되었습니다.");
    }

    @GetMapping("/item/orders/review")
    public ResponseEntity getReview(@ModelAttribute ReviewSearch reviewSearch) {
        List<Review> reviewList = reviewService.getReview(reviewSearch);
        ArrayList<Object> list = new ArrayList<>();
        for (Review review : reviewList) {
            GetReviewListDto dto = new GetReviewListDto(review);
            list.add(dto);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(list);
    }
}
