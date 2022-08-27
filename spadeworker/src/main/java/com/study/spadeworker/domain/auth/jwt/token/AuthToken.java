package com.study.spadeworker.domain.auth.jwt.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    public AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    public AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }

    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiry)
                .compact();
    }

    private String createAuthToken(String id, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiry)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validate() {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
            throw e;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다.");
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
    }

    public Claims getTokenClaims() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Access Token 이 만료될 경우, 재발급을 위해 만료된 토큰을 가져온다.
     */
    public Claims getExpiredTokenClaims() {
        try {
            getTokenClaims();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
        return null;
    }
}
