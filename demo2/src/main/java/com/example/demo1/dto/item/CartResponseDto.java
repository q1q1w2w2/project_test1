package com.example.demo1.dto.item;

import com.example.demo1.domain.Cart;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CartResponseDto {

    private Long userIdx;

    private Long itemIdx;

    private int ea;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public CartResponseDto(Cart cart) {
        this.userIdx = cart.getUser().getId();
        this.itemIdx = cart.getItem().getIdx();
        this.ea = cart.getEa();
        this.createAt = cart.getCreateAt();
        this.updateAt = cart.getUpdateAt();
    }
}
