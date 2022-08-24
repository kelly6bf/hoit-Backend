package com.study.spadeworker.domain.auth.jwt.service;

import com.study.spadeworker.domain.auth.jwt.AuthToken;
import com.study.spadeworker.domain.auth.jwt.AuthTokenProvider;
import com.study.spadeworker.domain.auth.jwt.UserRefreshToken;
import com.study.spadeworker.domain.auth.jwt.UserRefreshTokenRepository;
import com.study.spadeworker.domain.user.entity.constant.RoleType;
import com.study.spadeworker.global.config.properties.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.study.spadeworker.domain.auth.exception.AuthErrorCode.INVALID_TOKEN;
import static com.study.spadeworker.domain.auth.exception.AuthErrorCode.NOT_EXPIRED_TOKEN;

@Transactional
@RequiredArgsConstructor
@Service
public class TokenService {
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final static long THREE_DAYS_MSEC = 259200000;

    public Map<String, AuthToken> reissueToken(String accessToken, String refreshToken) {
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        Claims claims = getTokenClaims(authToken);
        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        // 리프레쉬 토큰 검증
        authRefreshToken.validate();

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            throw new JwtException(INVALID_TOKEN.getMessage());
        }

        // 새 accessToken 발급
        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        Map<String, AuthToken> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", authRefreshToken);
        AuthToken newRefreshToken = checkRefreshTokenExpireDate(authRefreshToken, userRefreshToken, now);
        // 리프래쉬 토큰의 만료시간 체크 후 3일 이하면 재발급
        if (newRefreshToken != null) {
            tokens.put("newRefreshToken", newRefreshToken);
        }

        return tokens;
    }

    private Claims getTokenClaims(AuthToken token) {
        Claims claims = null;
        try {
            token.validate();
        } catch (ExpiredJwtException e) {
            claims = token.getExpiredTokenClaims();
        }

        if (claims == null) {
            throw new JwtException(NOT_EXPIRED_TOKEN.getMessage());
        }

        return claims;
    }

    private AuthToken checkRefreshTokenExpireDate(AuthToken authRefreshToken, UserRefreshToken userRefreshToken, Date now) {
        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            AuthToken newRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(newRefreshToken.getToken());
            return newRefreshToken;
        }
        return null;
    }
}
