package com.example.demo1.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCartDto {

    private Long itemIdx;
    private int ea;
}
