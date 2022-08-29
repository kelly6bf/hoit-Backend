package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.article.entity.ArticleHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashtag, Long> {

    void deleteArticleHashtagsByArticle(Article article);

    List<ArticleHashtag> findAllByArticle(Article article);
}
