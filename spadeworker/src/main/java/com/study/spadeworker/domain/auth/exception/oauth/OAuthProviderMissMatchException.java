package com.study.spadeworker.domain.auth.exception.oauth;

import com.study.spadeworker.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class OAuthProviderMissMatchException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detailMessage;

    public OAuthProviderMissMatchException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

    public OAuthProviderMissMatchException(ErrorCode errorCode, String detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
