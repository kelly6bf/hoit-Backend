package com.study.spadeworker.domain.article.service;

import com.study.spadeworker.domain.article.dto.ArticleCommentDto;
import com.study.spadeworker.domain.article.dto.ChildArticleCommentDto;
import com.study.spadeworker.domain.article.entity.ArticleComment;
import com.study.spadeworker.domain.article.repository.ArticleCommentRepository;
import com.study.spadeworker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
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
    private List<ChildArticleCommentDto> getChildArticleCommentList(ArticleComment parentComment) {
        List<ArticleComment> childArticleCommentList = articleCommentRepository.findByParentComment(parentComment);
        List<ChildArticleCommentDto> childArticleCommentDtoList = new ArrayList<>();

        for (ArticleComment articleComment : childArticleCommentList) {
            childArticleCommentDtoList.add(
                    ChildArticleCommentDto.from(
                            articleComment,
                            userService.getUserAccountDto(articleComment.getUser()),
                            userService.getUserAccountDto(articleComment.getRecipient())
                    )
            );
        }

        return childArticleCommentDtoList;
    }
}
