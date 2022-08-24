package com.study.spadeworker.domain.auth.jwt.filter;

import com.study.spadeworker.domain.auth.exception.AuthErrorCode;
import com.study.spadeworker.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter 에서 발생한 예외 처리
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception.equals(AuthErrorCode.EXPIRED_TOKEN.getCode())) {
            setResponse(response, AuthErrorCode.EXPIRED_TOKEN);
        } else if (exception.equals(AuthErrorCode.INVALID_TOKEN.getCode())) {
            setResponse(response, AuthErrorCode.INVALID_TOKEN);
        } else if (exception.equals(AuthErrorCode.AUTHENTICATION_CLIENT_EXCEPTION.getCode())) {
            setResponse(response, AuthErrorCode.AUTHENTICATION_CLIENT_EXCEPTION);
        }
    }

    //한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, AuthErrorCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode);
        response.getWriter().print(errorResponse.convertJson());
    }
}

