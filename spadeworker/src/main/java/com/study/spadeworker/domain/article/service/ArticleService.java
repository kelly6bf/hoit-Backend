package com.study.spadeworker.domain.article.service;

import com.study.spadeworker.domain.article.dto.ArticleWithCommentsDto;
import com.study.spadeworker.domain.article.dto.article.ArticleDto;
import com.study.spadeworker.domain.article.dto.article.ArticlesViewOptionDto;
import com.study.spadeworker.domain.article.dto.article.CreateArticleDto;
import com.study.spadeworker.domain.article.dto.article.UpdateArticleDto;
import com.study.spadeworker.domain.article.dto.articleComment.ArticleCommentDto;
import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.article.entity.ArticleCategory;
import com.study.spadeworker.domain.article.repository.ArticleCategoryRepository;
import com.study.spadeworker.domain.article.repository.ArticleRepository;
import com.study.spadeworker.domain.board.service.BoardService;
import com.study.spadeworker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository articleCategoryRepository;
    private final UserService userService;
    private final BoardService boardService;
    private final HashtagService hashtagService;
    private final ArticleCommentService articleCommentService;


    /**
     * 게시글 생성 비즈니스
     */
    public Long createArticle(CreateArticleDto.Request request) {

        Article savedArticle = articleRepository.saveAndFlush(
                Article.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .articleCategory(getArticleCategoryEntity(request.getArticleCategory()))
                        .user(userService.getCurrentUser())
                        .board(boardService.getBoard(request.getBoardId()))
                        .build()
        );
        // 게시글 & 해시태그 연관관계 저장
        hashtagService.saveArticleHashtag(request.getHashtagList(), savedArticle);

        return savedArticle.getId();
    }

    /**
     * 게시글 수정 비즈니스
     */
    public Long updateArticle(Long articleId, UpdateArticleDto.Request request) {

        Article article = getArticleEntity(articleId);
        article.update(
                request.getTitle(),
                request.getContent(),
                getArticleCategoryEntity(request.getArticleCategory())
        );

        // 해시태그 변경 적용
        hashtagService.updateArticleHashtag(request.getHashtagList(), article);

        return articleId;
    }

    /**
     * 게시글 삭제 비즈니스
     */
    public void deleteArticle(Long articleId) {
        Article article = getArticleEntity(articleId);
        hashtagService.deleteArticleHashtag(article);
        articleRepository.delete(article);
    }

    /**
     * 게시글 id 단건 조회 비즈니스
     */
    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        Article article = getArticleEntity(articleId);
        return convertArticleToArticleDto(article);
    }

    /**
     * 특정 게시글 내용과 댓글 리스트 조회 비즈니스
     */
    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        ArticleDto articleDto = getArticle(articleId);
        List<ArticleCommentDto> articleCommentDtoList = articleCommentService.getArticleCommentList(articleId);

        return ArticleWithCommentsDto.builder()
                .article(articleDto)
                .commentList(articleCommentDtoList)
                .build();
    }

    /**
     * 특정 게시판의 조건에 맞는 모든 게시글 조회 및 정렬 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public Page<ArticleDto> getArticles(
            Long boardId,
            ArticlesViewOptionDto articlesViewOptionDto,
            Pageable pageable) {

        int size = articleRepository.getArticlesByBoard(boardId, articlesViewOptionDto, pageable).getContent().size();

        return articleRepository
                .getArticlesByBoard(
                        boardId,
                        articlesViewOptionDto,
                        pageable
                ).map(this::convertArticleToArticleDto);
    }

    // 게시글 Entity 조회 메서드
    @Transactional(readOnly = true)
    public Article getArticleEntity(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
    }

    // 게시글 카테고리 Entity 조회
    @Transactional(readOnly = true)
    protected ArticleCategory getArticleCategoryEntity(String category) {
        return articleCategoryRepository.findByTitle(category);
    }

    // Article -> ArticleDto 변환
    private ArticleDto convertArticleToArticleDto(Article article) {
        userService.getUserAccountDto(article.getUser());

        return ArticleDto.from(
                article,
                userService.getUserAccountDto(article.getUser()),
                hashtagService.getArticleHashtagList(article)
        );
    }
}
