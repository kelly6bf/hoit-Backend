package com.study.spadeworker.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    /**
     * Common Error
     */
    INVALID_INPUT_VALUE(400, "C-001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C-002", "Invalid Http Request Method"),
//    HANDLE_ACCESS_DENIED(403, "C-003", "Access is Denied"),
    INTERNAL_SERVER_ERROR(500, "S-001", "Server Error!");

    private final int status;
    private final String code;
    private final String message;
}
