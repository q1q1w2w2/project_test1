package com.example.demo1.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrdersItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    private int price;
    private int quantity;

    @Builder
    public OrdersItem(Item item, Orders orders, int price, int quantity) {
        this.item = item;
        this.orders = orders;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrdersItem createOrdersItem(Item item, int price, int quantity) {
        OrdersItem ordersItem = OrdersItem.builder()
                .item(item)
                .price(price)
                .quantity(quantity)
                .build();

        item.removeQuantity(quantity);

        return ordersItem;
    }
}
