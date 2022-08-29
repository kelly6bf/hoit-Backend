package com.study.spadeworker.domain.article.service;

import com.study.spadeworker.domain.article.dto.ArticleDetailDto;
import com.study.spadeworker.domain.article.dto.CreateArticleDto;
import com.study.spadeworker.domain.article.dto.UpdateArticleDto;
import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.article.entity.ArticleCategory;
import com.study.spadeworker.domain.article.repository.ArticleCategoryRepository;
import com.study.spadeworker.domain.article.repository.ArticleRepository;
import com.study.spadeworker.domain.board.service.BoardService;
import com.study.spadeworker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
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

    /**
     * 게시글 생성 비즈니스
     */
    public Long createArticle(CreateArticleDto.Request request) {
        Article savedArticle = articleRepository.saveAndFlush(
                Article.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .articleCategory(getArticleCategory(request.getArticleCategory()))
                        .user(userService.getCurrentUser())
                        .board(boardService.getBoardById(request.getBoardId()))
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
        Article article = getArticle(articleId);
        article.update(
                request.getTitle(),
                request.getContent(),
                getArticleCategory(request.getArticleCategory())
                );

        // 해시태그 변경 적용
        hashtagService.updateArticleHashtag(request.getHashtagList(), article);

        return articleId;
    }

    // 게시글 카테고리 조회
    private ArticleCategory getArticleCategory(String category) {
        return articleCategoryRepository.findByTitle(category);
    }

    /**
     * 게시글 삭제 비즈니스
     */
    public void deleteArticle(Long articleId) {
        Article article = getArticle(articleId);
        hashtagService.deleteArticleHashtag(article);
        articleRepository.delete(article);
    }

    // 게시글 조회 메서드
    private Article getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
    }

    /**
     * 게시글 단건 상세 조회 비즈니스
     */
    @Transactional(readOnly = true)
    public ArticleDetailDto getArticleDetail(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        userService.getUserAccountInfo(article.getUser());

        return ArticleDetailDto.from(
                article,
                userService.getUserAccountInfo(article.getUser()),
                hashtagService.getArticleHashtagList(article)
                );
    }
}
