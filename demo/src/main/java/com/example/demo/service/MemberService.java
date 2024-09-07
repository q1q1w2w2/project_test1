package com.example.demo.service;

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
        String password = passwordEncoder.encode(form.getPassword());
        return memberRepository.save(new Member(form.getName(), form.getLoginId(), password));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .orElse(null);
    }

    public boolean isLoginIdDuplicated(String loginId) {
        try {
            // 중복될 경우 true
            return memberRepository.findByLoginId(loginId).isPresent();
        } catch (Exception e) {
            log.error("isLoginIdDuplication Exception");
            throw new RuntimeException("중복 체크 오류 발생", e);
        }
    }


}
