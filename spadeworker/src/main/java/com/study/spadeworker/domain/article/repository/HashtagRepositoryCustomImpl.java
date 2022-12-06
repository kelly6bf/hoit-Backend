package com.study.spadeworker.domain.article.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.spadeworker.domain.article.entity.Hashtag;
import com.study.spadeworker.domain.article.entity.QArticleHashtag;
import com.study.spadeworker.domain.article.entity.QHashtag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

public class HashtagRepositoryCustomImpl extends QuerydslRepositorySupport implements HashtagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HashtagRepositoryCustomImpl(EntityManager em) {
        super(Hashtag.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Hashtag> getPopularHashtags() {
        QHashtag hashtag = QHashtag.hashtag;
        QArticleHashtag articleHashtag = QArticleHashtag.articleHashtag;

        List<Hashtag> hashtags = queryFactory.selectFrom(hashtag)
                .orderBy(

                )
                .limit(10)
                .fetch();

        return hashtags;
    }
}
