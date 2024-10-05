package com.example.demo1.web;

import com.example.demo1.domain.User;
import com.example.demo1.dto.user.RefreshTokenDto;
import com.example.demo1.jwt.TokenProvider;
import com.example.demo1.service.login.RefreshTokenService;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/token-refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody RefreshTokenDto request) throws Exception {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "refresh token이 null입니다."));
        }

        String subject = tokenProvider.extractUserIdFromRefreshToken(refreshToken);

        try {
            if (tokenProvider.validateToken(refreshToken)) {
                // 여기서 refresh token도 새롭게 발급 + 이전 refresh token 무효화
                String accessToken = tokenProvider.createNewAccessToken(refreshToken, "ROLE_USER"); // 추후 변경
                String newRefreshToken = tokenProvider.createRefreshToken(subject);

                // 사용한 refresh token 만료시간 업데이트
                refreshTokenService.updateRefreshToken(refreshToken);

                return ResponseEntity.ok(Map.of(
                        "accessToken", accessToken,
                        "refreshToken", newRefreshToken
                ));
            } else {
                // refresh token이 유효하지 않을 경우
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "refresh token이 유효하지 않습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 인증 없이도 접근 가능 -> permitAll 작동 안됨(401)
    @PostMapping("/authority/all")
    @PreAuthorize("true")
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

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "ok";
    }
}
