package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.dto.article.ArticlesViewOptionDto;
import com.study.spadeworker.domain.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    /**
     * 조건에 맞는 게시글 목록 조회
     */
    Page<Article> getArticlesByBoard(
            Long boardId,
            ArticlesViewOptionDto articlesViewOptionDto,
            Pageable pageable
    );
}
