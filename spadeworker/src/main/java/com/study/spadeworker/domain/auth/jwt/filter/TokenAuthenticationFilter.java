package com.study.spadeworker.domain.auth.jwt.filter;

import com.study.spadeworker.domain.auth.exception.jwt.TokenValidFailedException;
import com.study.spadeworker.domain.auth.jwt.token.AuthToken;
import com.study.spadeworker.domain.auth.jwt.token.AuthTokenProvider;
import com.study.spadeworker.global.util.HeaderUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.study.spadeworker.domain.auth.exception.AuthErrorCode.*;

/**
 * 사용자 인증 시 Token 을 검증하는 Servlet Filter
 */
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 토큰이 없을 경우
        String tokenStr = HeaderUtil.getAccessToken(request);
        if (tokenStr == null) {
            request.setAttribute("exception", NO_TOKEN.getCode());
            filterChain.doFilter(request, response);
            return;
        }

        AuthToken token = tokenProvider.convertAuthToken(tokenStr);

        // token 을 재발급 하는 경우
        String path = request.getRequestURI();
        if ("/auth/refresh".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (token.validate()) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", EXPIRED_TOKEN.getCode());
        } catch (JwtException e) {
            request.setAttribute("exception", INVALID_TOKEN.getCode());
        } catch (TokenValidFailedException e) {
            request.setAttribute("exception", AUTHENTICATION_CLIENT_EXCEPTION.getCode());
        }

        filterChain.doFilter(request, response);
    }
}
