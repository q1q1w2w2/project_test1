package com.example.demo1.web;

import com.example.demo1.dto.LoginDto;
import com.example.demo1.jwt.JwtFilter;
import com.example.demo1.jwt.TokenProvider;
import com.example.demo1.service.LoginService;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Validated @RequestBody LoginDto dto) throws Exception {
        String jwt = loginService.login(dto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(response);
    }
}
