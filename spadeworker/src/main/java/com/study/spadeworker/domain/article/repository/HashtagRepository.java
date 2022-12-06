package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface HashtagRepository extends
        JpaRepository<Hashtag, Long>,
        QuerydslPredicateExecutor<Hashtag>,
        HashtagRepositoryCustom
{
    Hashtag findByTitle(String hashtag);
    Boolean existsHashtagByTitle(String hashtag);

    @Query(value = "select * from hashtag order by (select count(*) from article_hashtag where hashtag_id = hashtag.id) desc", nativeQuery = true)
    List<Hashtag> getPopularHashtags();

}
