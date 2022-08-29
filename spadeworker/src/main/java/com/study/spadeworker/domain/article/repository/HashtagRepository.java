package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByTitle(String hashtag);
    Boolean existsHashtagByTitle(String hashtag);
}
