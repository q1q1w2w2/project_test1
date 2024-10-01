package com.example.demo1;

import com.example.demo1.dto.JoinDto;
import com.example.demo1.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final UserService userService;

    @PostConstruct
    public void init() {
        userService.joinAdmin(JoinDto.builder()
                .username("admin")
                .loginId("admin")
                .password("admin")
                .birth(LocalDate.of(2000, 1, 1))
                .address("서울")
                .detail("구로")
                .tel("010-1231-1313")
                .build()
        );

        userService.join(JoinDto.builder()
                .username("user1")
                .loginId("test1")
                .password("test1")
                .birth(LocalDate.of(1999, 12, 3))
                .address("경기")
                .detail("파주")
                .tel("010-4561-3393")
                .build()
        );
    }
}
