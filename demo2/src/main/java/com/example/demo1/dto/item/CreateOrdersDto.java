package com.example.demo1.dto.item;

import com.example.demo1.domain.Orders;
import com.example.demo1.domain.OrdersItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrdersDto {

    private Long itemIdx;
    private int quantity;

}

