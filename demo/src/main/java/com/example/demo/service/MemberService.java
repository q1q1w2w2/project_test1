package com.example.demo.service;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.exception.UserExistException;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(MemberJoinDto dto) {

        if (memberRepository.findByLoginId(dto.getLoginId()).isPresent()) {
            throw new UserExistException("이미 존재하는 아이디입니다.");
        }

        Authority authority = authorityRepository.findByAuthorityName("ROLE_MEMBER")
                .orElseThrow(() -> new RuntimeException("권한이 존재하지 않습니다."));

        Member member = Member.builder()
                .name(dto.getName())
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .authorities(Collections.singleton(authority))
                .build();

        return memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public void addAdmin(MemberJoinDto dto) {

        Authority roleAdmin = authorityRepository.findByAuthorityName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN 권한을 찾을 수 없습니다."));

        Authority roleMember = authorityRepository.findByAuthorityName("ROLE_MEMBER")
                .orElseThrow(() -> new RuntimeException("ROLE_MEMBER 권한을 찾을 수 없습니다."));

        Member member = Member.builder()
                .name(dto.getName())
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .authorities(Set.of(roleAdmin, roleMember))
                .build();

        memberRepository.save(member);
    }

    // loginId 기준으로 정보 가져옴
    public Optional<Member> getUserWithAuthorities(String loginId) {
        return memberRepository.findOneWithAuthoritiesByLoginId(loginId);
    }

    // SecurityContext에 저장된 loginId 정보를 가져옴
    public Optional<Member> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByLoginId);
    }


}
