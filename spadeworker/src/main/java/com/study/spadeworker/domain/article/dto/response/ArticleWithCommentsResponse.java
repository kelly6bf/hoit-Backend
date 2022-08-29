package com.study.spadeworker.domain.article.dto.response;

import com.study.spadeworker.domain.article.dto.ArticleCommentDto;
import com.study.spadeworker.domain.article.dto.ArticleDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleWithCommentsResponse {

    private ArticleDetailDto article;
    private List<ArticleCommentDto> comments;

    @Builder
    public ArticleWithCommentsResponse(ArticleDetailDto article, List<ArticleCommentDto> comments) {
        this.article = article;
        this.comments = comments;
    }
}
