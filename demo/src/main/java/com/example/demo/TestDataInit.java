package com.example.demo;

import com.example.demo.domain.Member;
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
        memberService.join(new Member("MemberA", "test1", "test1"));
        memberService.join(new Member("MemberB", "test2", "test2"));
    }
}
