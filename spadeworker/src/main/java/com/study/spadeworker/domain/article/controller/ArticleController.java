package com.study.spadeworker.domain.article.controller;

import com.study.spadeworker.domain.article.dto.ArticleWithCommentsDto;
import com.study.spadeworker.domain.article.dto.article.ArticleDto;
import com.study.spadeworker.domain.article.dto.article.ArticlesViewOptionDto;
import com.study.spadeworker.domain.article.dto.article.CreateArticleDto;
import com.study.spadeworker.domain.article.dto.article.UpdateArticleDto;
import com.study.spadeworker.domain.article.service.ArticleService;
import com.study.spadeworker.global.response.CommonResult;
import com.study.spadeworker.global.response.ListResult;
import com.study.spadeworker.global.response.ResponseService;
import com.study.spadeworker.global.response.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ArticleController {

    private final ResponseService responseService;
    private final ArticleService articleService;

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
                OK.value(),
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
                OK.value(),
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
                OK.value(),
                "성공적으로 게시글이 삭제되었습니다."
        );
    }

    /**
     * 게시글 단건 및 게시글 댓글 리스트 조회 API
     */
    @GetMapping("/article/{articleId}")
    public SingleResult<ArticleWithCommentsDto> getArticleDetail(
            @PathVariable final Long articleId
    ) {
        ArticleWithCommentsDto articleWithCommentsDto = articleService.getArticleWithComments(articleId);

        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 게시글이 조회되었습니다.",
                articleWithCommentsDto
        );
    }

    /**
     * 해당 게시판의 조건에 맞는 게시글 전체 조회 및 정렬 API
     */
    @GetMapping("/board/{boardId}/articles")
    public ListResult<ArticleDto> getArticles(
            @PathVariable final Long boardId,
            ArticlesViewOptionDto articlesViewOptionDto,
            @PageableDefault(size = 3) Pageable pageable
    ) {
        return responseService.getListResult(
                OK.value(),
                "성공적으로 게시글이 조회되었습니다.",
                articleService.getArticles(
                        boardId,
                        articlesViewOptionDto,
                        pageable
                ).getContent()
        );
    }
}
