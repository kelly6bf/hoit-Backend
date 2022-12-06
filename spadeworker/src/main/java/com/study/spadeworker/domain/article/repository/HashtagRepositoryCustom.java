package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.Hashtag;

import java.util.List;

public interface HashtagRepositoryCustom {
    /**
     * 인기 태그 상위 n개를 가져오는 메서드
     */
    List<Hashtag> getPopularHashtags();
}
