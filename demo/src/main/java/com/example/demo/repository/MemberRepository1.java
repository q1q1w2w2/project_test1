package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository1 extends JpaRepository<Member, Long> {

    Member save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByLoginId(String loginId);

    List<Member> findAll();
}
