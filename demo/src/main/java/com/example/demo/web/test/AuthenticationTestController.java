package com.example.demo.web.test;

import com.example.demo.domain.Member;
import com.example.demo.jwt.TokenProvider;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthenticationTestController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @PostMapping("/test")
    public ResponseEntity getCurrentMember() {
        // jwt 토큰 이용하여 현재 로그인 된 member 정보 반환
        log.info("*** getCurrentMember ***");
        Optional<Member> userWithAuthorities = memberService.getMyUserWithAuthorities();
        return ResponseEntity.status(HttpStatus.OK).body(userWithAuthorities);
    }

    @PostMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')") // 메서드 실행 전 인증
    public ResponseEntity getUserInfo() {
        // 현재 Security Context 에 저장된 인증 정보 꺼내서 반환
        log.info("** [/user] **");

        if (memberService.getMyUserWithAuthorities().isPresent()) {
            return ResponseEntity.ok(memberService.getMyUserWithAuthorities().get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/user/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity onlyAdmin() {
        log.info("** [/user/admin] ** ");
        return ResponseEntity.ok("OK");
    }

}
