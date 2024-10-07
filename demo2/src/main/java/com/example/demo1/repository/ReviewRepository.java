package com.example.demo1.repository;

import com.example.demo1.domain.*;
import com.example.demo1.dto.item.ReviewSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    public Review save(Review review) {
        em.persist(review);
        return review;
    }

    public List<Review> findAll(ReviewSearch reviewSearch) {

        QItem i = QItem.item;
        QOrders o = QOrders.orders;
        QOrdersItem oi = QOrdersItem.ordersItem;
        QCategory c = QCategory.category;
        QReview r = QReview.review1;

        BooleanBuilder builder = new BooleanBuilder();

        if (reviewSearch.getItemIdx() != null) {
            builder.and(i.idx.eq(reviewSearch.getItemIdx()));
        }

        if (reviewSearch.getCategoryIdx() != null) {
            builder.and(c.idx.eq(reviewSearch.getCategoryIdx()));
        }

        return queryFactory
                .selectFrom(r)
                .join(r.orders, o)
                .join(o.ordersItems, oi)
                .join(oi.item, i)
                .join(i.category, c)
                .where(builder)
                .limit(1000)
                .fetch();
    }
}
