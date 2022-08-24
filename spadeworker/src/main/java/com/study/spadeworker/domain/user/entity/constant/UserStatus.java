package com.study.spadeworker.domain.user.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {
    ACTIVE("활성화 된 계정입니다."),
    BLOCKED("차단 된 계정입니다."),
    DORMANT("휴면 상태인 계정입니다."),
    DELETE("탈퇴한 계정 입니다.");

    private final String message;
}