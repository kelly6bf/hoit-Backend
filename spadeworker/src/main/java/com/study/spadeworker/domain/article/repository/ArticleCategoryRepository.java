package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {

    ArticleCategory findByTitle(String category);
}
