package com.example.demo1.web;

import com.example.demo1.dto.LoginDto;
import com.example.demo1.jwt.JwtFilter;
import com.example.demo1.jwt.TokenProvider;
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

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Validated @RequestBody LoginDto dto) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());

        // authenticationManger 내부적으로 UserDetailsService 호출
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(response);
    }

    /**
     * ** login 동작 **
     * Dto로 loginId, password를 받음
     * 받은 정보로 UsernamePasswordAuthenticationToken 생성
     * authenticationManager로 위의 token을 호출하여 인증 시도
     * authenticationManager는 내부적으로 UserDetailsService의 loadUserByUsername 호출하여 정보 조회
     * 사용자가 존재하면 UserDetails 반환, 없다면 UsernameNotFoundException
     * 요청으로 받은 password와 UserDetails의 password 비교(BadCredentialException)
     * 성공 시 Authentication 객체 반환
     * Authentication객체를 SecurityContext에 저장하고 jwt 생성
     */

}
