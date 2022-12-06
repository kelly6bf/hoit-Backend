package com.study.spadeworker.domain.auth.controller;

import com.study.spadeworker.domain.auth.jwt.service.TokenService;
import com.study.spadeworker.domain.auth.jwt.token.AuthToken;
import com.study.spadeworker.global.config.properties.AppProperties;
import com.study.spadeworker.global.response.ResponseService;
import com.study.spadeworker.global.response.SingleResult;
import com.study.spadeworker.global.util.CookieUtil;
import com.study.spadeworker.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AppProperties appProperties;
    private final TokenService tokenService;
    private final ResponseService responseService;

    private final static String REFRESH_TOKEN = "refresh_token";

    @GetMapping("/refresh")
    public SingleResult<Map<String, String>> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        Map<String, AuthToken> tokens = tokenService.reissueToken(accessToken, refreshToken);

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        // refresh token cookie 재설정
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        if (tokens.containsKey("newRefreshToken")) {
            CookieUtil.addCookie(response, REFRESH_TOKEN, tokens.get("newRefreshToken").getToken(), cookieMaxAge);
        } else {
            CookieUtil.addCookie(response, REFRESH_TOKEN, tokens.get("refreshToken").getToken(), cookieMaxAge);
        }

        Map<String, String> responseResult = new HashMap<>();
        responseResult.put("newAccessToken", tokens.get("accessToken").getToken());
        if (tokens.containsKey("newRefreshToken"))
            responseResult.put("newRefreshToken", tokens.get("newRefreshToken").getToken());

        return responseService.getSingleResult(
                HttpStatus.OK.value(),
                "성공적으로 토큰이 발급되었습니다!",
                responseResult);
    }
}
