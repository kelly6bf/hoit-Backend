package com.study.spadeworker.domain.user.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AccountRoleType {
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    USER("ROLE_USER", "일반 유저 권한"),
    GUEST("GUEST", "게스트 권한");

    private final String code;
    private final String displayName;

    public static AccountRoleType of(String code) {
        return Arrays.stream(AccountRoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}