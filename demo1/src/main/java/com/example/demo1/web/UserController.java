package com.example.demo1.web;

import com.example.demo1.domain.User;
import com.example.demo1.dto.JoinDto;
import com.example.demo1.jwt.TokenProvider;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @GetMapping("/join")
    @ResponseBody
    public String join() {
        return "OK";
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@Validated @RequestBody JoinDto dto) {
        log.info("*** join POST ***");
        User user = userService.join(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원가입이 완료되었습니다.");
        response.put("user", user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
