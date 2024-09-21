package com.example.demo1.service;

import com.example.demo1.dto.LoginDto;
import com.example.demo1.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public String login(LoginDto dto) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());

        // authenticationManger 내부적으로 UserDetailsService 호출
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
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
