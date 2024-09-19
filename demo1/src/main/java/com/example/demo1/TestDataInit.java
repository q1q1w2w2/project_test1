package com.example.demo1;

import com.example.demo1.dto.JoinDto;
import com.example.demo1.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final UserService userService;

    @PostConstruct
    public void init() {
        userService.join(new JoinDto("memberA", "test1", "test1"));
        userService.join(new JoinDto("memberB", "test2", "test2"));
    }
}
