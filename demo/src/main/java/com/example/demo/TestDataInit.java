package com.example.demo;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.service.LoginService;
import com.example.demo.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class TestDataInit {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        memberService.join(new MemberJoinDto("admin", "admin", "admin"));
        memberService.addAuthority(1L, new Authority("ROLE_ADMIN"));
        memberService.addAuthority(1L, new Authority("ROLE_USER"));

        memberService.join(new MemberJoinDto("MemberA", "test1", "test1"));
        memberService.addAuthority(2L, new Authority("ROLE_USER"));

        memberService.join(new MemberJoinDto("MemberB", "test2", "test2"));
        memberService.addAuthority(3L, new Authority("ROLE_USER"));
    }
}
