package com.example.demo1.dto.item;

import com.example.demo1.domain.Category;
import com.example.demo1.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {

    private Long itemIdx;
    private Long userIdx;
    private String itemName;
    private Long category;
    private int quantity;
    private int price;
    private String explanation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemResponseDto(Item item) {
        this.itemIdx = item.getIdx();
        this.userIdx = item.getUser().getId();
        this.itemName = item.getItemName();
        this.category = item.getCategory().getIdx();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.explanation = item.getExplanation();
        this.createdAt = item.getCreatedAt();
        this.updatedAt = item.getUpdatedAt();
    }
}
