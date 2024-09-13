package com.example.demo.web;

import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.MemberLoginDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.jwt.JwtFilter;
import com.example.demo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class LoginController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("member") MemberLoginDto member) {
        return "member/login";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Validated @RequestBody MemberLoginDto loginDto) {
        // 로그인 -> 인증정보 확인해서 Jwt 토큰 반환
        log.info(loginDto.toString());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());
        log.info("authenticationToken: {}", authenticationToken);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("authentication: {}", authentication);

        String jwt = tokenProvider.createToken(authentication);
        log.info("jwt: {}", jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        log.info("httpHeaders: {}", httpHeaders);

        // 응답에 jwt 포함, 클라이언트 측에서 받아서 localStorage 에 보관
        // return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);

        LoginResponseDto loginResponseDto = new LoginResponseDto(jwt, "로그인되었습니다.");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(loginResponseDto);
    }

}
