package com.study.spadeworker.domain.auth.oauth.service;

import com.study.spadeworker.domain.auth.UserPrincipal;
import com.study.spadeworker.domain.auth.exception.oauth.OAuthProviderMissMatchException;
import com.study.spadeworker.domain.auth.oauth.info.OAuth2UserInfo;
import com.study.spadeworker.domain.auth.oauth.info.OAuth2UserInfoFactory;
import com.study.spadeworker.domain.user.entity.Role;
import com.study.spadeworker.domain.user.entity.User;
import com.study.spadeworker.domain.user.entity.UserRole;
import com.study.spadeworker.domain.user.entity.constant.ProviderType;
import com.study.spadeworker.domain.user.entity.constant.RoleType;
import com.study.spadeworker.domain.user.repository.RoleRepository;
import com.study.spadeworker.domain.user.repository.UserRepository;
import com.study.spadeworker.domain.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.study.spadeworker.domain.auth.exception.AuthErrorCode.MISS_MATCH_PROVIDER;

/**
 소셜 로그인 성공 시 후속 조치 담당
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        // OAuth Login 을 통해 프로바이더 별 User 의 정보를 받아서 저장
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        User savedUser = userRepository.findByLoginId(userInfo.getId());

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        MISS_MATCH_PROVIDER,
                        "이미 가입된 계정입니다!");
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        User user = User.of(
                userInfo.getId(),
                null,
                userInfo.getName(),
                userInfo.getProfileImgUrl(),
                userInfo.getEmail(),
                providerType
        );
        Role role = roleRepository.findByAuthority(RoleType.USER);

        userRoleRepository.saveAndFlush(
                UserRole.builder()
                        .user(user)
                        .role(role)
                        .build()
        );
        return userRepository.saveAndFlush(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getName().equals(userInfo.getName())) {
            user.changeUsername(userInfo.getName());
        }

        if (userInfo.getProfileImgUrl() != null && !user.getProfileImgUrl().equals(userInfo.getProfileImgUrl())) {
            user.changeProfileImgUrl(userInfo.getProfileImgUrl());
        }

        return user;
    }
}
