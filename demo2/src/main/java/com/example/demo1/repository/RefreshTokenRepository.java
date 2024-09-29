package com.example.demo1.repository;

import com.example.demo1.domain.RefreshToken;
import com.example.demo1.exception.TokenValidationException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final EntityManager em;

    public void save(RefreshToken refreshToken) {
        em.persist(refreshToken);
    }

    public Optional<RefreshToken> findByRefreshToken(String token) {
        return em.createQuery("select r from RefreshToken r where refreshToken = :token", RefreshToken.class)
                .setParameter("token", token)
                .getResultList()
                .stream().findFirst();
    }

}
