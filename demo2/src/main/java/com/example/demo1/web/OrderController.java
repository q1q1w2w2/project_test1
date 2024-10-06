package com.example.demo1.web;

import com.example.demo1.domain.Cart;
import com.example.demo1.domain.Orders;
import com.example.demo1.domain.User;
import com.example.demo1.dto.item.AddCartDto;
import com.example.demo1.dto.item.CartResponseDto;
import com.example.demo1.dto.item.CreateOrdersDto;
import com.example.demo1.dto.item.OrderResponseDto;
import com.example.demo1.service.OrderService;
import com.example.demo1.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final TokenUtil tokenUtil;

    @PostMapping("/item/cart/add")
    public ResponseEntity<CartResponseDto> addCart(@RequestBody AddCartDto dto, @RequestHeader String authorization) throws Exception {

        User user = tokenUtil.extractUserByToken(authorization);
        Cart cart = orderService.saveCart(dto, user);
        CartResponseDto response = new CartResponseDto(cart);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/item/orders")
    public ResponseEntity createOrders(@RequestBody CreateOrdersDto dto, @RequestHeader String authorization) throws Exception {
        User user = tokenUtil.extractUserByToken(authorization);
        Orders orders = orderService.saveOrders(user, dto.getItemIdx(), dto.getQuantity());

        OrderResponseDto response = new OrderResponseDto(orders);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
