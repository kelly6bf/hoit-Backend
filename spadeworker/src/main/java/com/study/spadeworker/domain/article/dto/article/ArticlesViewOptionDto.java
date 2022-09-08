package com.study.spadeworker.domain.article.dto.article;

import com.study.spadeworker.domain.article.constant.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticlesViewOptionDto {

    private final OrderType orderType;

    private final String hashtag;

    private final String keyword;
}
