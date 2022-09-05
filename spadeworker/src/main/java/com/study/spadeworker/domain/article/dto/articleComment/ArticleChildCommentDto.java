package com.study.spadeworker.domain.article.dto.articleComment;

import com.study.spadeworker.domain.article.entity.ArticleComment;
import com.study.spadeworker.domain.user.dto.UserAccountDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleChildCommentDto {

    private Long id;
    private Long parentCommentId;
    private String content;
    private int likesCount;
    private int dislikesCount;
    private UserAccountDto recipient;
    private LocalDateTime createdAt;
    private Long articleId;
    private UserAccountDto user;

    private ArticleChildCommentDto(
                    Long id,
                    Long parentCommentId,
                    UserAccountDto recipient,
                    String content,
                    int likesCount,
                    int dislikesCount,
                    LocalDateTime createdAt,
                    Long articleId,
                    UserAccountDto userAccountDto
    ) {
        this.id = id;
        this.parentCommentId = parentCommentId;
        this.recipient = recipient;
        this.content = content;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.createdAt = createdAt;
        this.articleId = articleId;
        this.user = userAccountDto;
    }

    public static ArticleChildCommentDto from(
            ArticleComment articleComment,
            UserAccountDto userAccountDto,
            UserAccountDto recipient
    ) {
        return new ArticleChildCommentDto(
                articleComment.getId(),
                articleComment.getParentComment().getId(),
                recipient,
                articleComment.getContent(),
                articleComment.getLikesCount(),
                articleComment.getDislikesCount(),
                articleComment.getCreatedAt(),
                articleComment.getArticle().getId(),
                userAccountDto
        );
    }
}
