package com.study.spadeworker.domain.auth.exception.jwt;

public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super("토큰 인증에 실패하였습니다.");
    }

    private TokenValidFailedException(String message) {
        super(message);
    }
}
