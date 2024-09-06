package com.example.demo;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        Member memberA = new Member("MemberA", "test1", "test1");
        Member memberB = new Member("MemberB", "test2", "test2");
        memberService.join(memberA);
        memberService.join(memberB);
    }
}
