package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void join() {
        // given
        Member member = createMember("member", "test", "test");

        // when
        Long saveId = memberService.join(member);

        // then
        assertThat(memberService.findById(saveId)).isEqualTo(member);
    }

    @Test
    void findAll() {
        // given
        Member member1 = createMember("member1", "test1", "test1");
        Member member2 = createMember("member2", "test2", "test2");

        // when
        memberService.join(member1);
        memberService.join(member2);

        // then
        assertThat(memberService.findAll().size()).isEqualTo(2);
    }

    @Test
    void login_Success() {
        // given
        String loginId = "testId";
        String password = "testPassword";
        Member member = createMember("member", loginId, password);
        memberService.join(member);

        // when
        Member loginMember = memberService.login(loginId, password);

        // then
        assertThat(loginMember).isNotNull();
        assertThat(loginMember.getLoginId()).isEqualTo(loginId);
    }

    @Test
    void login_Failure() {

    }

    private static Member createMember(String name, String loginId, String password) {
        return new Member(name, loginId, password);
    }

}