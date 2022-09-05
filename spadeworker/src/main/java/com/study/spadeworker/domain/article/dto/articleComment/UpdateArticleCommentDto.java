package com.study.spadeworker.domain.article.dto.articleComment;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateArticleCommentDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotNull(message = "댓글 본문은 필수 입니다.")
        private String content;
        @Setter
        private Long articleCommentId;
        @Setter
        private Long recipientId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long articleCommentId;

        public static UpdateArticleCommentDto.Response of(@NonNull Long articleCommentId) {
            return UpdateArticleCommentDto.Response.builder()
                    .articleCommentId(articleCommentId)
                    .build();
        }
    }
}
