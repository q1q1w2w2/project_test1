package com.example.demo1.repository;

import com.example.demo1.domain.BlackList;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlackListRepository {

    private final EntityManager em;

    public void save(BlackList blackList) {
        em.persist(blackList);
    }

    public boolean existsInBlackList(String refreshToken) {
        return !em.createQuery("select b from BlackList b where b.invalidRefreshToken = :refreshToken")
                .setParameter("refreshToken", refreshToken)
                .getResultList()
                .isEmpty(); // 비어있으면 false, 존재하면 true
    }
}
