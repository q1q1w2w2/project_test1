package com.example.demo1.web;

import com.example.demo1.dto.LogoutDto;
import com.example.demo1.dto.RefreshTokenDto;
import com.example.demo1.jwt.JwtFilter;
import com.example.demo1.service.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader(JwtFilter.AUTHORIZATION_HEADER) String accessToken,
            @Validated @RequestBody RefreshTokenDto dto
    ) {
        String refreshToken = dto.getRefreshToken();
        accessToken = accessToken.substring(7);
        logoutService.logout(refreshToken, accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 되었습니다.");
    }
}
