package com.study.spadeworker.domain.article.controller;

import com.study.spadeworker.domain.article.dto.ArticleCommentDto;
import com.study.spadeworker.domain.article.dto.ArticleDetailDto;
import com.study.spadeworker.domain.article.dto.CreateArticleDto;
import com.study.spadeworker.domain.article.dto.UpdateArticleDto;
import com.study.spadeworker.domain.article.dto.response.ArticleWithCommentsResponse;
import com.study.spadeworker.domain.article.service.ArticleCommentService;
import com.study.spadeworker.domain.article.service.ArticleService;
import com.study.spadeworker.global.response.CommonResult;
import com.study.spadeworker.global.response.ResponseService;
import com.study.spadeworker.global.response.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;
    private final ResponseService responseService;

    /**
     * 게시물 생성 API
     */
    @PostMapping("/board/{boardId}/article")
    public SingleResult<CreateArticleDto.Response> createArticle(
            @PathVariable final Long boardId,
            @Valid @RequestBody final CreateArticleDto.Request request
    ) {
        request.setBoardId(boardId);
        Long createdArticleId = articleService.createArticle(request);

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 게시글이 작성되었습니다.",
                CreateArticleDto.Response.of(createdArticleId)
        );
    }

    /**
     * 게시물 수정 API
     */
    @PatchMapping("/article/{articleId}")
    public SingleResult<UpdateArticleDto.Response> updateArticle(
            @PathVariable final Long articleId,
            @Valid @RequestBody final UpdateArticleDto.Request request
    ) {
        Long updatedArticleId = articleService.updateArticle(articleId, request);

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 게시글이 수정되었습니다.",
                UpdateArticleDto.Response.of(updatedArticleId)
        );
    }

    /**
     * 게시물 삭제 API
     */
    @DeleteMapping("/article/{articleId}")
    public CommonResult deleteArticle(
            @PathVariable final Long articleId
    ) {
        articleService.deleteArticle(articleId);

        return responseService.getSuccessResult(
                HttpStatus.OK.value(),
                "성공적으로 게시글이 삭제되었습니다."
        );
    }

    /**
     * 게시물 단건 상세 내용 및 댓글 조회 API
     */
    @GetMapping("/article/{articleId}")
    public SingleResult<ArticleWithCommentsResponse> getArticleDetail(
            @PathVariable final Long articleId
    ) {
        ArticleDetailDto articleDetail = articleService.getArticleDetail(articleId);
        List<ArticleCommentDto> articleCommentList = articleCommentService.searchArticleComments(articleId);

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 게시글이 조회되었습니다.",
                ArticleWithCommentsResponse.builder()
                        .article(articleDetail)
                        .comments(articleCommentList)
                        .build()
        );
    }
}
