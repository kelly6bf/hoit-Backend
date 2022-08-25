package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
