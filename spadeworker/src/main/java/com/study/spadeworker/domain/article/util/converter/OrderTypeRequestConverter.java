package com.study.spadeworker.domain.article.util.converter;

import com.study.spadeworker.domain.article.constant.OrderType;
import org.springframework.core.convert.converter.Converter;

public class OrderTypeRequestConverter implements Converter<String, OrderType> {
    @Override
    public OrderType convert(String orderType) {
        return OrderType.of(orderType);
    }
}
