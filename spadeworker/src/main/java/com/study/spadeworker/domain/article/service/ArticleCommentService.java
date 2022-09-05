package com.study.spadeworker.domain.article.service;

import com.study.spadeworker.domain.article.dto.articleComment.*;
import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.article.entity.ArticleComment;
import com.study.spadeworker.domain.article.repository.ArticleCommentRepository;
import com.study.spadeworker.domain.article.repository.ArticleRepository;
import com.study.spadeworker.domain.user.entity.User;
import com.study.spadeworker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;

    /**
     * 특정 게시글에 달린 모든 댓글 리스트를 조회하는 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public List<ArticleCommentDto> getArticleCommentList(Long articleId) {
        List<ArticleComment> articleCommentList = articleCommentRepository.findByArticle_IdAndIsChildFalse(articleId);
        List<ArticleCommentDto> articleCommentDtoList = new ArrayList<>();
        for (ArticleComment articleComment : articleCommentList) {
            articleCommentDtoList.add(
                    ArticleCommentDto.from(
                            articleComment,
                            userService.getUserAccountDto(articleComment.getUser()),
                            getChildArticleCommentList(articleComment)
                    )
            );
        }

        return articleCommentDtoList;
    }

    // 특정 최상위 댓글의 대댓글 리스트를 조회하는 비즈니스
    @Transactional(readOnly = true)
    protected List<ArticleChildCommentDto> getChildArticleCommentList(ArticleComment parentComment) {
        List<ArticleComment> childArticleCommentList = articleCommentRepository.findByParentComment(parentComment);
        List<ArticleChildCommentDto> articleChildCommentDtoList = new ArrayList<>();

        for (ArticleComment articleComment : childArticleCommentList) {
            articleChildCommentDtoList.add(
                    ArticleChildCommentDto.from(
                            articleComment,
                            userService.getUserAccountDto(articleComment.getUser()),
                            userService.getUserAccountDto(articleComment.getRecipient())
                    )
            );
        }

        return articleChildCommentDtoList;
    }

    /**
     * 게시글 댓글 생성 비즈니스 로직
     */
    public Long createArticleComment(
            Long articleId,
            CreateArticleCommentDto.Request request
    ) {
        Article article = getArticleEntity(articleId);
        User user = userService.getCurrentUser();

        return articleCommentRepository.save(
                ArticleComment.createComment(
                        request.getContent(),
                        false,
                        article,
                        user,
                        null,
                        null
                )
        ).getId();
    }

    /**
     * 게시글 대댓글 생성 비즈니스 로직
     */
    public Long createChildArticleComment(
            CreateArticleChildCommentDto.Request request
    ) {
        Article article = getArticleEntity(request.getArticleId());
        User user = userService.getCurrentUser();

        return articleCommentRepository.save(
                ArticleComment.createComment(
                        request.getContent(),
                        true,
                        article,
                        user,
                        getArticleCommentEntity(request.getParentCommentId()),
                        (request.getRecipientId() != null) ?
                                userService.getUserEntity(request.getRecipientId()) : null
                )
        ).getId();
    }

    // 게시글 댓글 단건 조회
    @Transactional(readOnly = true)
    protected ArticleComment getArticleCommentEntity(Long articleCommentId) {
        return articleCommentRepository.findById(articleCommentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
    }

    // 게시글 Entity 조회 메서드
    @Transactional(readOnly = true)
    public Article getArticleEntity(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
    }

    /**
     * 게시글 댓글 수정 비즈니스 로직
     */
    public Long updateArticleComment(
            UpdateArticleCommentDto.Request request
    ) {
        ArticleComment articleComment = getArticleCommentEntity(request.getArticleCommentId());
        articleComment.updateArticleComment(
                request.getContent(),
                (request.getRecipientId() != null) ?
                        userService.getUserEntity(request.getRecipientId()) : null
        );

        return articleComment.getId();
    }

    /**
     * 게시글 댓글 삭제 비즈니스 로직
     */
    public void deleteArticleComment(
            Long articleCommentId
    ) {
        articleCommentRepository.delete(
                getArticleCommentEntity(articleCommentId)
        );
    }
}
