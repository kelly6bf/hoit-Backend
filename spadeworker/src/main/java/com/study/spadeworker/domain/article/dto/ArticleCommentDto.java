package com.study.spadeworker.domain.article.dto;

import com.study.spadeworker.domain.article.entity.ArticleComment;
import com.study.spadeworker.domain.user.dto.UserAccountDto;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ArticleCommentDto {

    private Long id;
    private String content;
    private int likesCount;
    private int dislikesCount;
    private LocalDateTime createdAt;
    private UserAccountDto user;

    private ArticleCommentDto(Long id, String content, int likesCount, int dislikesCount, LocalDateTime createdAt, UserAccountDto userAccountDto) {
        this.id = id;
        this.content = content;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.createdAt = createdAt;
        this.user = userAccountDto;
    }

    public static ArticleCommentDto from(ArticleComment articleComment, UserAccountDto userAccountDto) {
        return new ArticleCommentDto(
                articleComment.getId(),
                articleComment.getContent(),
                articleComment.getLikesCount(),
                articleComment.getDislikesCount(),
                articleComment.getCreatedAt(),
                userAccountDto
        );
    }
}
