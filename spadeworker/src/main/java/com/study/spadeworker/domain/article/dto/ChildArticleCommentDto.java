package com.study.spadeworker.domain.article.dto;

import com.study.spadeworker.domain.article.entity.ArticleComment;
import com.study.spadeworker.domain.user.dto.UserAccountDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChildArticleCommentDto {

    private Long id;
    private Long parentCommentId;
    private String content;
    private int likesCount;
    private int dislikesCount;
    private UserAccountDto recipient;
    private LocalDateTime createdAt;
    private UserAccountDto user;

    private ChildArticleCommentDto(
                    Long id,
                    Long parentCommentId,
                    UserAccountDto recipient,
                    String content,
                    int likesCount,
                    int dislikesCount,
                    LocalDateTime createdAt,
                    UserAccountDto userAccountDto
    ) {
        this.id = id;
        this.parentCommentId = parentCommentId;
        this.recipient = recipient;
        this.content = content;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.createdAt = createdAt;
        this.user = userAccountDto;
    }

    public static ChildArticleCommentDto from(
            ArticleComment articleComment,
            UserAccountDto userAccountDto,
            UserAccountDto recipient
    ) {
        return new ChildArticleCommentDto(
                articleComment.getId(),
                articleComment.getParentComment().getId(),
                recipient,
                articleComment.getContent(),
                articleComment.getLikesCount(),
                articleComment.getDislikesCount(),
                articleComment.getCreatedAt(),
                userAccountDto
        );
    }
}
