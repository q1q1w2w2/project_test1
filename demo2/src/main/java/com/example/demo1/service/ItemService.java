package com.example.demo1.service;

import com.example.demo1.domain.Category;
import com.example.demo1.domain.Item;
import com.example.demo1.domain.User;
import com.example.demo1.dto.item.ItemDto;
import com.example.demo1.repository.CategoryRepository;
import com.example.demo1.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Item save(ItemDto dto, User user) {

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        Item item = Item.builder()
                .itemName(dto.getItemName())
                .userIdx(user)
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .category(category)
                .explanation(dto.getExplanation())
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();

        return itemRepository.save(item);
    }

    @Transactional
    public Item update(ItemDto dto, Long itemIdx, String loginId) {
        Item findItem = itemRepository.findById(itemIdx)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 상품을 등록한 유저와 현재 로그인 한 유저가 다르면 수정할 수 없음
        if (!findItem.getUser().getLoginId().equals(loginId)) {
            throw new RuntimeException("상품을 등록한 사용자가 아닙니다.");
        }

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        Item item = Item.builder()
                .itemName(dto.getItemName())
                .category(category)
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .explanation(dto.getExplanation())
                .build();

        return findItem.update(item);
    }
}
