package com.study.spadeworker.domain.article.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.article.entity.QArticle;
import com.study.spadeworker.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends
        JpaRepository<Article, Long> {
    /**
     * 최신순 정렬
     */
    Page<Article> findByBoard_IdOrderByCreatedAtDesc(
            Long boardId,
            Pageable pageable);

    /**
     * 댓글 많은순 조회
     */
    @Query("select a from Article a where a.board.id = :boardId" +
            " order by a.comments.size desc ")
    Page<Article> findByBoard_IdOrderByCommentsCount(
            @Param("boardId") Long boardId,
            Pageable pageable);

    /**
     * 좋아요 많은순 조회
     */
    Page<Article> findByBoard_IdOrderByLikesCountDesc(
            Long boardId,
            Pageable pageable);
}
