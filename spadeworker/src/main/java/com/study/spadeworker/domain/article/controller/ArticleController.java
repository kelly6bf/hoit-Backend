package com.study.spadeworker.domain.article.controller;

import com.study.spadeworker.domain.article.dto.ArticleDetailDto;
import com.study.spadeworker.domain.article.dto.CreateArticleDto;
import com.study.spadeworker.domain.article.dto.UpdateArticleDto;
import com.study.spadeworker.domain.article.service.ArticleService;
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
@RequestMapping("/api/v1/article")
@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final ResponseService responseService;

    /**
     * 게시물 생성 API
     */
    @PostMapping()
    public SingleResult<CreateArticleDto.Response> createArticle(
            @Valid @RequestBody final CreateArticleDto.Request request
    ) {
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
    @PatchMapping("/{articleId}")
    public SingleResult<UpdateArticleDto.Response> updateArticle(
            @PathVariable Long articleId,
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
    @DeleteMapping("/{articleId}")
    public CommonResult deleteArticle(
            @PathVariable Long articleId
    ) {
        articleService.deleteArticle(articleId);

        return responseService.getSuccessResult(
                HttpStatus.OK.value(),
                "성공적으로 게시글이 삭제되었습니다."
        );
    }

    /**
     * 게시물 단건 상세조회 API
     */
    @GetMapping("/{articleId}")
    public SingleResult<ArticleDetailDto> getArticleDetail(
            @PathVariable Long articleId
    ) {
        ArticleDetailDto articleDetail = articleService.getArticleDetail(articleId);

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 게시글이 조회되었습니다.",
                articleDetail
        );
    }
}
