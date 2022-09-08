package com.study.spadeworker.domain.article.dto.article;

import com.study.spadeworker.domain.article.constant.OrderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticlesViewOptionDto {

    private final OrderType order;

    private final String hashtag;

    private final String keyword;
}
