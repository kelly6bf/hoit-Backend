package com.study.spadeworker.global.error;

import com.study.spadeworker.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

import static com.study.spadeworker.global.error.GlobalErrorCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Java Bean Validation 예외 핸들링
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handle MethodArgumentNotValidException");
        return new ResponseEntity<>(
                ErrorResponse.of(INVALID_INPUT_VALUE, e.getBindingResult()),
                HttpStatus.valueOf(INVALID_INPUT_VALUE.getStatus())
        );
    }

    /**
     * EntityNotFound 예외 핸들링
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("handle EntityNotFoundException");
        return new ResponseEntity<>(
                ErrorResponse.of(ENTITY_NOT_FOUND, e.getMessage()),
                HttpStatus.valueOf(ENTITY_NOT_FOUND.getStatus()
                ));
    }

    /**
     * 비지니스 요구사항에 맞지 않은 경우 발생하는 예외를 핸들링
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("handle BusinessException");
        return new ResponseEntity<>(
                ErrorResponse.of(e.getErrorCode()),
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    /**
     * 유효하지 않은 클라이언트의 요청 값 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("handle IllegalArgumentException");
        return new ResponseEntity<>(
                ErrorResponse.of(INVALID_INPUT_VALUE, e.getMessage()),
                HttpStatus.valueOf(INVALID_INPUT_VALUE.getStatus())
        );
    }

    /**
     * 잘못된 HTTP Method 요청 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handle HttpRequestMethodNotSupportedException");
        return new ResponseEntity<>(
                ErrorResponse.of(METHOD_NOT_ALLOWED),
                HttpStatus.valueOf(METHOD_NOT_ALLOWED.getStatus())
        );
    }

    /**
     * 잘못된 타입 변환 예외 처리
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handle BindException");
        return new ResponseEntity<>(
                ErrorResponse.of(INVALID_INPUT_VALUE, e.getBindingResult()),
                HttpStatus.valueOf(INVALID_INPUT_VALUE.getStatus())
        );
    }

    /**
     * 모든 예외를 처리
     * 웬만해서 여기까지 오면 안됨
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handle Exception", e);
        return new ResponseEntity<>(
                ErrorResponse.of(INTERNAL_SERVER_ERROR),
                HttpStatus.valueOf(INTERNAL_SERVER_ERROR.getStatus())
        );
    }
}
