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
    public List<ArticleCommentDto> getArticleCommentList(Long articleId) {
        List<ArticleComment> articleCommentList = articleCommentRepository.findByArticle_Id(articleId);
        List<ArticleCommentDto> articleCommentDtoList = new ArrayList<>();
        for (ArticleComment ac : articleCommentList) {
            articleCommentDtoList.add(
                    ArticleCommentDto.from(ac, userService.getUserAccountDto(ac.getUser()))
            );
        }

        return articleCommentDtoList;
    }
}
