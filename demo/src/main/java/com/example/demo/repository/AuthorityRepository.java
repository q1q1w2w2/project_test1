package com.example.demo.repository;

import com.example.demo.domain.Authority;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorityRepository {

    private final EntityManager em;

//    public String addAuthority(String authority) {
//        em.persist(new Authority(authority));
//        return authority;
//    }

    public Optional<Authority> findByAuthorityName(String authority) {
        return Optional.ofNullable(em.find(Authority.class, authority));
    }
}
