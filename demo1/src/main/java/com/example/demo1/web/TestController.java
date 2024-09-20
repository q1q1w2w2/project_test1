package com.example.demo1.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("permitAll()")
public class TestController {

    // 권한에 따른 api 접근
    @PostMapping("/authority/all")
    public String authorityAll() {
        return "[authorityAll] ok";
    }

    // @PreAuthorize("ROLE_ADMIN") // 이 경우 단순히 문자열 "ROLE_ADMIN"이 참인지 확인 -> 사용 불가
    @PostMapping("/authority/admin")
    @PreAuthorize("hasRole('ADMIN')") // Spring Securiy의 표현식, ROLE_ 접두사 자동 추가
    public String authorityAdmin() {
        return "[authorityAdmin] ok";
    }
}
