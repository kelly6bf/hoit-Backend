package com.study.spadeworker.domain.article.dto.article;

import com.study.spadeworker.domain.article.constant.OrderType;
import com.study.spadeworker.domain.article.validation.OrderTypeValid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticlesViewOptionDto {

    @OrderTypeValid
    private final String order;

    private final String hashtag;

    private final String keyword;

    public OrderType getOrderType() {
        return (order != null) ? OrderType.valueOf(order.toUpperCase()) : null;
    }
}
