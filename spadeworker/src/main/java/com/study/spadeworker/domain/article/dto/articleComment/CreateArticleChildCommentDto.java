package com.study.spadeworker.domain.article.dto.articleComment;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateArticleChildCommentDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "게시글 id는 필수입니다.")
        private Long articleId;
        @NotBlank(message = "댓글 본문은 필수입니다.")
        @Length(min = 1, max = 10000, message = "댓글 본문 길이 제한은 1이상 10000이하 입니다.")
        private String content;
        @Setter
        private Long parentCommentId;
        @Setter
        private Long recipientId;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long articleCommentId;

        public static Response of(@NonNull final Long articleCommentId) {
            return new Response(articleCommentId);
        }
    }
}
