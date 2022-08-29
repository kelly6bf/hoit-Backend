package com.study.spadeworker.domain.article.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateArticleDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotNull(message = "게시글 제목은 필수 입니다.")
        @Size(min = 1, max = 200, message = "제목 길이 제한은 1이상 200이하 입니다.")
        private String title;

        @NotNull(message = "게시글 내용은 필수입니다.")
        @Size(min = 1, message = "본문 길이 제한은 1이상 입니다.")
        private String content;

        @NotNull(message = "게시글 카테고리는 필수입니다.")
        @Size(min = 1, max = 255, message = "카테고리 길이 제한은 1이상 255이하 입니다.")
        private String articleCategory;

        @Setter
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long articleId;

        public static Response of(@NonNull Long articleId) {
            return Response.builder()
                    .articleId(articleId)
                    .build();
        }
    }
}
