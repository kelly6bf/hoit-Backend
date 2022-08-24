package com.study.spadeworker.domain.auth.exception;

import com.study.spadeworker.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    /**
     * 확인 불명 오류 코드
     * 0 ~ 99
     */
    AUTHENTICATION_CLIENT_EXCEPTION(401, "A000", "사용자 인증에 실패하였습니다."),
    AUTHENTICATION_SERVICE_EXCEPTION(500, "A001", "인증 로직이 실패하였습니다."),

    /**
     * JWT
     * 100 ~ 199
     */
    INVALID_TOKEN(400, "A100", "잘못된 토큰 입니다."),
    FAILED_GENERATE_TOKEN(500, "A101", "토큰 생성에 실패하였습니다."),
    EXPIRED_TOKEN(401, "A102", "만료된 토큰 입니다."),
    NOT_EXPIRED_TOKEN(401, "A103", "만료되지 않은 토큰 입니다."),
    INVALID_REFRESH_TOKEN(400, "A104", "잘못된 refresh token 입니다."),
    NO_TOKEN(400, "A105", "요청에 토큰이 존재하지 않습니다."),

    /**
     * OAuth
     * 200 ~ 299
     */
    MISS_MATCH_PROVIDER(400, "A200", "OAuth 공급자가 일치하지 않습니다."),
    INVALID_LOGIN_REDIRECT_URI(400, "A201", "유효하지 않은 리다이렉트 주소입니다."),
    INVALID_PROVIDER_TYPE(400, "A202", "올바른 Provider 타입이 아닙니다.");

    private final int status;
    private final String code;
    private final String message;

}
