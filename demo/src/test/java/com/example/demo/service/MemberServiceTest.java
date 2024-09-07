package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

//    @Test
//    void join() {
//        // given
//        String name = "member";
//        String loginId = "test";
//        String password = "test";
//        MemberJoinDto memberDto = createMemberDto(name, loginId, password);
//
//        // when
//        Member joinMember = memberService.join(memberDto);
//
//        // then
//        assertThat(joinMember).isNotNull(); // 명확성을 위해 추가
//        assertThat(joinMember.getName()).isEqualTo(name);
//        assertThat(joinMember.getLoginId()).isEqualTo(loginId);
//        assertThat(passwordEncoder.matches(password, joinMember.getPassword())).isTrue();
//    }
//
//    @Test
//    void findAll() {
//        // given
//        MemberJoinDto memberDto1 = createMemberDto("member1", "test1", "test1");
//        MemberJoinDto memberDto2 = createMemberDto("member2", "test2", "test2");
//
//        // when
//        memberService.join(memberDto1);
//        memberService.join(memberDto2);
//
//        // then
//        assertThat(memberService.findAll().size()).isEqualTo(2);
//    }
//
//    @Test
//    void login_Success() {
//        // given
//        String loginId = "testId";
//        String password = "testPassword";
//        MemberJoinDto memberDto = createMemberDto("member", loginId, password);
//        memberService.join(memberDto);
//
//        // when
//        Member loginMember = memberService.login(loginId, password);
//
//        // then
//        assertThat(loginMember).isNotNull();
//        assertThat(loginMember.getLoginId()).isEqualTo(loginId);
//    }
//
//    @Test
//    void login_Failure() {
//
//    }
//
//    private static MemberJoinDto createMemberDto(String name, String loginId, String password) {
//        return new MemberJoinDto(name, loginId, password);
//    }

}