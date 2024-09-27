package com.example.demo1.web;

import com.example.demo1.domain.User;
import com.example.demo1.jwt.JwtFilter;
import com.example.demo1.jwt.TokenProvider;
import com.example.demo1.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request) throws Exception {
        // 클라이언트 측에서 access token의 만료를 확인하고 재발급을 위해 요청
        String bearer = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);
        String refreshToken = bearer.substring(7);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "refresh token이 null입니다."));
        }

        try {
            if (tokenProvider.validateToken(refreshToken)) {
                String accessToken = tokenProvider.createNewAccessToken(refreshToken);
                return ResponseEntity.ok(Map.of("accessToken", accessToken));
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
