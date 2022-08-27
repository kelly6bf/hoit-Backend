package com.study.spadeworker.domain.auth.oauth.handler;

import com.study.spadeworker.domain.auth.exception.oauth.InvalidLoginRedirectUriException;
import com.study.spadeworker.domain.auth.jwt.token.AuthToken;
import com.study.spadeworker.domain.auth.jwt.token.AuthTokenProvider;
import com.study.spadeworker.domain.auth.jwt.token.UserRefreshToken;
import com.study.spadeworker.domain.auth.jwt.token.UserRefreshTokenRepository;
import com.study.spadeworker.domain.auth.oauth.info.OAuth2UserInfo;
import com.study.spadeworker.domain.auth.oauth.info.OAuth2UserInfoFactory;
import com.study.spadeworker.domain.auth.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.study.spadeworker.domain.user.entity.constant.ProviderType;
import com.study.spadeworker.domain.user.entity.constant.RoleType;
import com.study.spadeworker.global.config.properties.AppProperties;
import com.study.spadeworker.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        // 리다이렉트
        System.out.println("targetUrl = " + targetUrl);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * 프론트로 리다이렉트 할 url 생성
     */
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        System.out.println("사용자가 원하는 리다이렉션 주소 내용 = " + redirectUri.get());

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
//            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
            throw new InvalidLoginRedirectUriException();
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

        RoleType roleType = findRole(authorities);

        Date now = new Date();
        // access Token 생성
        AuthToken accessToken = tokenProvider.createAuthToken(
                userInfo.getId(),
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        // refresh 토큰 설정
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // DB 저장
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userInfo.getId());
        if (userRefreshToken != null) {
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        } else {
            userRefreshToken = new UserRefreshToken(userInfo.getId(), refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;

        CookieUtil.deleteCookie(request, response, OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN);
        CookieUtil.addCookie(response, OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);
        System.out.println("refresh token : " + refreshToken.getToken());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken.getToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 여러 권한 중 하나를 검증하기 위해 리펙토링을 진행
    private RoleType findRole(Collection<? extends GrantedAuthority> authorities) {
        List<RoleType> roleTypes = Stream.of(RoleType.values()).toList();

        for (RoleType role : roleTypes) {
            for (GrantedAuthority grantedAuthority : authorities) {
                if (role.getCode().equals(grantedAuthority.getAuthority())) {
                    return role;
                }
            }
        }

        return RoleType.USER;
    }

    /**
     * 프론트 에서 지정한 리다이렉트 url 검증 로직
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
