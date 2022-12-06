package com.study.spadeworker.domain.article.repository;

import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        ArticleRepositoryCustom {

    List<Article> getArticlesByUser(User user);

    List<Article> findAllByBoard_Id(Long boardId);
}
