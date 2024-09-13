package com.example.demo.service;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(MemberJoinDto form) {
        // 아이디 중복검증 필요
        String password = passwordEncoder.encode(form.getPassword());
        return memberRepository.save(Member.builder()
                .name(form.getName())
                .loginId(form.getLoginId())
                .password(password)
                .build());
    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public void addAuthority(Long id, Authority authority) {
        Member member = memberRepository.findById(id);
        member.getAuthorities().add(authority);
        memberRepository.save(member);
    }


}
