package com.study.spadeworker.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    /**
     * Common Error
     */
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력 값 입니다."),
    INTERNAL_SERVER_ERROR(500, "C002", "Server Error!");

    private final int status;
    private final String code;
    private final String message;
}
