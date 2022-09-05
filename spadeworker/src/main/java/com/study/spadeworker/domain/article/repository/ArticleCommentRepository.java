package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

    List<ArticleComment> findByArticle_IdAndIsChildFalse(Long articleId);

    List<ArticleComment> findByParentComment(ArticleComment parentComment);
}
