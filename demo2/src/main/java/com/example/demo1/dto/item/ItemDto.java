package com.example.demo1.dto.item;

import com.example.demo1.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {

    private String itemName;

    private Long category;

    private int quantity;

    private int price;

    private String explanation;

}
