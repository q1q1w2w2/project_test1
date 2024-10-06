package com.example.demo1.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id @GeneratedValue
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @Column(name = "item_name")
    private String itemName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private int quantity;

    private int price;

    private String explanation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Item(User userIdx, String itemName, Category category,int quantity, int price, String explanation, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.user = userIdx;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.explanation = explanation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Item update(Item item) {
        this.itemName = item.getItemName();
        this.category = item.getCategory();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.explanation = item.getExplanation();
        this.updatedAt = LocalDateTime.now().withNano(0);
        return this;
    }

    public void removeQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
