package com.study.spadeworker.domain.auth.exception;

import com.study.spadeworker.domain.auth.exception.jwt.TokenValidFailedException;
import com.study.spadeworker.domain.auth.exception.oauth.InvalidLoginRedirectUriException;
import com.study.spadeworker.domain.auth.exception.oauth.InvalidProviderTypeException;
import com.study.spadeworker.domain.auth.exception.oauth.OAuthProviderMissMatchException;
import com.study.spadeworker.global.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.study.spadeworker.domain.auth.exception.AuthErrorCode.*;

@Slf4j
@RestControllerAdvice()
public class AuthenticationExceptionHandler {

    /**
     * JWT 만료 예외 핸들링
     */
    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(EXPIRED_TOKEN);

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.valueOf(EXPIRED_TOKEN.getStatus())
        );
    }

    /**
     * 유효하지 않은 JWT 예외 핸들링
     */
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponse> handleJwtException(JwtException e) {
        final ErrorResponse errorResponse;

        if (e.getMessage().equals(NOT_EXPIRED_TOKEN.getMessage())) {
            errorResponse = ErrorResponse.of(NOT_EXPIRED_TOKEN);
        } else {
            errorResponse = ErrorResponse.of(INVALID_TOKEN);
        }

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.valueOf(INVALID_TOKEN.getStatus())
        );
    }

    @ExceptionHandler(TokenValidFailedException.class)
    protected ResponseEntity<ErrorResponse> handleTokenValidFailedException(TokenValidFailedException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(AUTHENTICATION_CLIENT_EXCEPTION);

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.valueOf(AUTHENTICATION_CLIENT_EXCEPTION.getStatus())
        );
    }

    @ExceptionHandler(OAuthProviderMissMatchException.class)
    protected ResponseEntity<ErrorResponse> handleOAuthProviderMissMatchException(OAuthProviderMissMatchException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(MISS_MATCH_PROVIDER);

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.valueOf(MISS_MATCH_PROVIDER.getStatus())
        );
    }

    @ExceptionHandler(InvalidProviderTypeException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidProviderTypeException(InvalidProviderTypeException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(INVALID_PROVIDER_TYPE);

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.valueOf(INVALID_PROVIDER_TYPE.getStatus())
        );
    }

    @ExceptionHandler(InvalidLoginRedirectUriException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidLoginRedirectUriException(InvalidLoginRedirectUriException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(INVALID_LOGIN_REDIRECT_URI);

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.valueOf(INVALID_LOGIN_REDIRECT_URI.getStatus())
        );
    }

}
