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
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
//
//    public Member login(String loginId, String password) {
//        Optional<Member> member = memberRepository.findByLoginId(loginId);
//        if (member.isPresent()) {
//            if (passwordEncoder.matches(password, member.get().getPassword())) {
//                return member.get();
//            } else {
//                throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
//            }
//        } else {
//            throw new IllegalArgumentException("아이디가 틀렸습니다.");
//        }
//    }
//
//    public boolean isLoginIdDuplicated(String loginId) {
//        try {
//            // 중복될 경우 true
//            return memberRepository.findByLoginId(loginId).isPresent();
//        } catch (Exception e) {
//            log.error("isLoginIdDuplication Exception");
//            throw new RuntimeException("중복 체크 오류 발생", e);
//        }
//    }
}
