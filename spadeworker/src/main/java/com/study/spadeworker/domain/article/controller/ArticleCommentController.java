package com.study.spadeworker.domain.article.controller;

import com.study.spadeworker.domain.article.dto.articleComment.CreateArticleCommentDto;
import com.study.spadeworker.domain.article.dto.articleComment.CreateArticleChildCommentDto;
import com.study.spadeworker.domain.article.dto.articleComment.UpdateArticleCommentDto;
import com.study.spadeworker.domain.article.service.ArticleCommentService;
import com.study.spadeworker.global.response.CommonResult;
import com.study.spadeworker.global.response.ResponseService;
import com.study.spadeworker.global.response.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ArticleCommentController {

    private final ResponseService responseService;
    private final ArticleCommentService articleCommentService;

    /**
     * 게시글 댓글 생성 API
     */
    @PostMapping("/article/{articleId}/article-comment")
    public SingleResult<CreateArticleCommentDto.Response> createArticleComment(
            @PathVariable final Long articleId,
            @Valid @RequestBody final CreateArticleCommentDto.Request request
    ) {
        request.setArticleId(articleId);

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 댓글이 작성되었습니다.",
                CreateArticleCommentDto.Response.of(
                        articleCommentService.createArticleComment(articleId, request)
                )
        );
    }

    /**
     * 게시글 대댓글 생성 API
     */
    @PostMapping("/article-comment/{articleCommentId}/child-comment")
    public SingleResult<CreateArticleCommentDto.Response> createChildArticleComment(
            @PathVariable final Long articleCommentId,
            @RequestParam(value = "recipient_id", required = false) final Long recipientId,
            @Valid @RequestBody final CreateArticleChildCommentDto.Request request
    ) {
        request.setParentCommentId(articleCommentId);
        request.setRecipientId(recipientId);

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 댓글이 작성되었습니다.",
                CreateArticleCommentDto.Response.of(
                        articleCommentService.createChildArticleComment(request)
                )
        );
    }

    /**
     * 게시글 댓글 수정 API
     */
    @PatchMapping("/article-comment/{articleCommentId}")
    public SingleResult<UpdateArticleCommentDto.Response> updateArticleComment(
            @PathVariable final Long articleCommentId,
            @RequestParam(value = "recipient_id", required = false) final Long recipientId,
            @Valid @RequestBody final UpdateArticleCommentDto.Request request
    ) {
        request.setArticleCommentId(articleCommentId);
        request.setRecipientId(recipientId);

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 댓글이 수정되었습니다.",
                UpdateArticleCommentDto.Response.of(
                        articleCommentService.updateArticleComment(request)
                )
        );
    }

    /**
     * 게시글 댓글 삭제 API
     */
    @DeleteMapping("/article-comment/{articleCommentId}")
    public CommonResult deleteArticleComment(
            @PathVariable final Long articleCommentId
    ) {
        articleCommentService.deleteArticleComment(articleCommentId);

        return responseService.getSuccessResult(
                HttpStatus.OK.value(),
                "성공적으로 댓글이 삭제되었습니다."
        );
    }
}
