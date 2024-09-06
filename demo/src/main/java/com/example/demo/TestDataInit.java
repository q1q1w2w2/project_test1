package com.example.demo;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class TestDataInit {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String password1 = passwordEncoder.encode("test1");
        String password2 = passwordEncoder.encode("test2");
        memberService.join(new Member("MemberA", "test1", password1));
        memberService.join(new Member("MemberB", "test2", password2));
    }
}
