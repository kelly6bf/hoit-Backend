package com.study.spadeworker.domain.article.constant;

public enum OrderType {
    RECENT("recent"),
    COMMENT("comment"),
    LIKES("likes");

    private final String typeName;

    OrderType(String typeName) {
        this.typeName = typeName;
    }

    public static OrderType of(String typeName) {
        if (typeName == null) {
            throw new IllegalArgumentException();
        }

        for (OrderType st : OrderType.values()) {
            if (st.typeName.equals(typeName)) {
                return st;
            }
        }

        throw new IllegalArgumentException("일치하는 정렬 타입이 없습니다.");
    }
}
