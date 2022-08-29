package com.study.spadeworker.domain.article.service;

import com.study.spadeworker.domain.article.dto.ArticleCommentDto;
import com.study.spadeworker.domain.article.entity.ArticleComment;
import com.study.spadeworker.domain.article.repository.ArticleCommentRepository;
import com.study.spadeworker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        List<ArticleComment> articleComments = articleCommentRepository.findByArticle_Id(articleId);
        List<ArticleCommentDto> articleCommentList = new ArrayList<>();
        for (ArticleComment ac : articleComments) {
            articleCommentList.add(
                    ArticleCommentDto.from(ac, userService.getUserAccountInfo(ac.getUser()))
            );
        }

        return articleCommentList;
    }
}
