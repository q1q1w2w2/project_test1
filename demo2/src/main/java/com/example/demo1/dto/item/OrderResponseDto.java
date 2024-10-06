package com.example.demo1.dto.item;

import com.example.demo1.domain.Orders;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {
    private Long idx;
    private Long userIdx;
//    private Long itemIdx;
    private int step;
//    private int quantity;
//    private int price;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public OrderResponseDto(Orders orders) {
        this.idx = orders.getIdx();
        this.userIdx = orders.getUser().getId();
//        this.itemIdx = orders.getOrdersItems().get(0).getId();
        this.step = orders.getStep();
//        this.quantity = orders.getOrdersItems().get(0).getQuantity();
//        this.price = orders.getOrdersItems().get(0).getPrice();
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }
}
