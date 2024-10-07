package com.example.demo1.service;

import com.example.demo1.domain.*;
import com.example.demo1.dto.item.AddCartDto;
import com.example.demo1.dto.item.UpdateOrderStepDto;
import com.example.demo1.repository.CartRepository;
import com.example.demo1.repository.ItemRepository;
import com.example.demo1.repository.OrdersRepository;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart saveCart(AddCartDto dto, User user) {
        Item item = itemRepository.findById(dto.getItemIdx())
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        if (dto.getEa() > item.getQuantity()) {
            throw new RuntimeException("수량이 부족합니다.");
        }

        Cart cart = Cart.builder()
                .user(user)
                .item(item)
                .ea(dto.getEa())
                .createAt(LocalDateTime.now().withNano(0))
                .updateAt(LocalDateTime.now().withNano(0))
                .build();

        return cartRepository.save(cart);
    }

    @Transactional
    public Orders saveOrders(User user, Long itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (quantity > item.getQuantity()) {
            throw new RuntimeException("수량이 부족합니다.");
        }

        OrdersItem ordersItem = OrdersItem.createOrdersItem(item, item.getPrice(), quantity);
        Orders orders = Orders.createOrders(user, ordersItem);

        return ordersRepository.save(orders);
    }

    @Transactional
    public void updateStep(UpdateOrderStepDto dto) {
        Orders orders = ordersRepository.findById(dto.getOrdersIdx())
                .orElseThrow(() -> new RuntimeException("주문 정보를 찾을 수 없습니다."));

        orders.updateStep(dto.getStep());
    }
}
