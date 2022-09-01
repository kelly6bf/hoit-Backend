package com.study.spadeworker.domain.article.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleWithCommentsDto {

    private ArticleDto article;
    private List<ArticleCommentDto> comments;

    @Builder
    public ArticleWithCommentsDto(ArticleDto article, List<ArticleCommentDto> comments) {
        this.article = article;
        this.comments = comments;
    }
}
