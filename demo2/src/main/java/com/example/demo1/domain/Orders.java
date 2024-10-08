package com.example.demo1.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Orders {

    @Id
    @GeneratedValue
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    private int step; // 주문완료:1, 배송시작:2, 배송완료:3, 취소:10

    private int review; // 미등록:0, 등록:1

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrdersItem> ordersItems = new ArrayList<>();

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    public Orders(User user, int step, int review, OrdersItem ordersItem, LocalDateTime createAt, LocalDateTime updateAt) {
        this.user = user;
        this.step = step;
        this.review = review;
        this.ordersItems.add(ordersItem);
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public void addOrdersItem(OrdersItem ordersItem) {
        ordersItems.add(ordersItem);
        ordersItem.setOrders(this);
    }

    public static Orders createOrders(User user, OrdersItem... ordersItems) {
        Orders orders = Orders.builder()
                .user(user)
                .step(3)
                .review(0)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        for (OrdersItem ordersItem : ordersItems) {
            orders.addOrdersItem(ordersItem);
        }

        return orders;
    }

    public void updateStep(int step) {
        this.step = step;
    }

    public void updateReview(int review) {
        this.review = review;
    }
}
