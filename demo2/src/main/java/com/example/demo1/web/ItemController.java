package com.example.demo1.web;

import com.example.demo1.domain.Item;
import com.example.demo1.domain.User;
import com.example.demo1.dto.item.ItemDto;
import com.example.demo1.dto.item.ItemResponseDto;
import com.example.demo1.service.ItemService;
import com.example.demo1.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final TokenUtil tokenUtil;

    @PostMapping("/save")
    public ResponseEntity<ItemResponseDto> saveItem(@Validated @RequestBody ItemDto dto, @RequestHeader String authorization) throws Exception {
        User user = tokenUtil.extractUserByToken(authorization);

        Item saveItem = itemService.save(dto, user);

        ItemResponseDto response = new ItemResponseDto(saveItem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update/{itemIdx}")
    public ResponseEntity<ItemResponseDto> updateItem(@Validated @RequestBody ItemDto dto,@PathVariable Long itemIdx, @RequestHeader String authorization) throws Exception {
        String loginId = tokenUtil.extractUserByToken(authorization).getLoginId();

        Item updateItem = itemService.update(dto, itemIdx, loginId);

        ItemResponseDto response = new ItemResponseDto(updateItem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
