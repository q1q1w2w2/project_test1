package com.example.demo;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.LoginService;
import com.example.demo.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class TestDataInit {

    private final MemberService memberService;

    @PostConstruct
    public void init() {

        memberService.addAdmin(new MemberJoinDto("admin", "admin", "admin"));

        memberService.join(new MemberJoinDto("MemberA", "test1", "test1"));
        memberService.join(new MemberJoinDto("MemberB", "test2", "test2"));
    }
}
