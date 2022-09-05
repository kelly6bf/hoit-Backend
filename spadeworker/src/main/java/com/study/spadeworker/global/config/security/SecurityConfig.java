package com.study.spadeworker.global.config.security;

import com.study.spadeworker.domain.auth.jwt.token.AuthTokenProvider;
import com.study.spadeworker.domain.auth.jwt.token.UserRefreshTokenRepository;
import com.study.spadeworker.domain.auth.jwt.filter.CustomAccessDeniedHandler;
import com.study.spadeworker.domain.auth.jwt.filter.CustomAuthenticationEntryPoint;
import com.study.spadeworker.domain.auth.jwt.filter.TokenAuthenticationFilter;
import com.study.spadeworker.domain.auth.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.study.spadeworker.domain.auth.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.study.spadeworker.domain.auth.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.study.spadeworker.domain.auth.oauth.service.CustomOAuth2UserService;
import com.study.spadeworker.domain.user.entity.constant.RoleType;
import com.study.spadeworker.global.config.properties.AppProperties;
import com.study.spadeworker.global.config.properties.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    // 의존성 주입
    public SecurityConfig(
            CorsProperties corsProperties,
            AppProperties appProperties,
            AuthTokenProvider tokenProvider,
            CustomOAuth2UserService oAuth2UserService,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            UserRefreshTokenRepository userRefreshTokenRepository) {
        this.corsProperties = corsProperties;
        this.appProperties = appProperties;
        this.tokenProvider = tokenProvider;
        this.oAuth2UserService = oAuth2UserService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // cors 설정
        http.cors();

        // 세션을 사용하지 않기 때문에, 세션 설정을 STATELESS 로 설정
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // token 을 사용하는 방식이기 때문에 csrf 를 disable 한다.
        http.csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http.exceptionHandling()
                // 토큰 인증 필터에서 발생한 예외를 처리
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                // 401, 403 예외 핸들러를 우리가 제작한 핸들러로 넣어줌
                .accessDeniedHandler(customAccessDeniedHandler);

        // 권한별 요청 설정
        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.GET, "/api/article/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/auth/refresh").permitAll()
//                .antMatchers("/api/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode())
                // 나머지는 모두 인증 필요
                .anyRequest().authenticated();

        // front 에서 login 시 요청할 url
        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository());

        // OAuth Server 리다이렉션 주소
        http.oauth2Login()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*");

        // 인증 후 user 정보를 파싱할 서비스 등록
        http.oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService);

        // OAuth2 성공/실패 시 처리 할 핸들러 등록
        http.oauth2Login()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

//         login 한 사용자 검증 필터 설정
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * security 설정 시, PW 암호화에 사용할 인코더 설정
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * auth 매니저 설정
     * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * 토큰 필터 설정
     * */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
                userRefreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /*
     * Cors 설정
     * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }

}