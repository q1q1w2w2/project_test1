package com.example.demo1.repository;

import com.example.demo1.domain.BlackList;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlackListRepository {

    private final EntityManager em;

    public void save(BlackList blackList) {
        em.persist(blackList);
    }

    public Optional<BlackList> findByInvalidRefreshToken(String refreshToken) {
        return em.createQuery("select b from BlackList b where b.invalidRefreshToken = :refreshToken", BlackList.class)
                .setParameter("refreshToken", refreshToken)
                .getResultList()
                .stream().findFirst();
    }

}
