package com.example.demo.repository;

import com.example.demo.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // loginId 로 조회, 사용자와 권한 반환
    public Optional<Member> findOneWithAuthoritiesByLoginId(String loginId) {
        return em.createQuery("select m from Member m left join fetch m.authorities where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList().stream().findFirst();
    }
}
