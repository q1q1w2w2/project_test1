package com.example.demo1.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx")
    private Item item;

    private int ea;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    public Cart(User user, Item item, int ea, LocalDateTime createAt, LocalDateTime updateAt) {
        this.user = user;
        this.item = item;
        this.ea = ea;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
