package com.example.demo1.web;

import com.example.demo1.domain.User;
import com.example.demo1.service.UserService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 인증 없이도 접근 가능 -> 작동 안됨(401)
    @PostMapping("/authority/all")
    @PreAuthorize("permitAll()")
    public String authorityAll() {
        return "[authorityAll] ok";
    }

    @PostMapping("/authority/user")
    @PreAuthorize("hasRole('USER')")
    public String authorityUser() {
        return "[authorityUser] ok";
    }

    @PostMapping("/authority/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String authorityAdmin() {
        return "[authorityAdmin] ok";
    }

    @PostMapping("/users")
    public ResponseEntity users() {
        List<User> all = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(all);
    }
}
