package com.study.spadeworker.domain.user.entity;

import com.study.spadeworker.domain.user.entity.constant.ProviderType;
import com.study.spadeworker.domain.user.entity.constant.RoleType;
import com.study.spadeworker.domain.user.entity.constant.UserStatus;
import com.study.spadeworker.global.config.audit.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 200, unique = true)
    private String loginId;

    @Column(nullable = false, length = 500)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 512)
    private String profileImgUrl;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserStatus status;

    private User(String loginId, String password, String name, String profileImgUrl, String email, ProviderType providerType, RoleType role) {
        this.loginId = loginId;
        this.password = password != null ? password : "NO_PASSWORD";
        this.name = name;
        this.profileImgUrl = profileImgUrl != null ? profileImgUrl : "";
        this.description = null;
        this.email = email != null ? email : "NO_EMAIL";
        this.providerType = providerType;
        this.role = role;
        this.status = UserStatus.ACTIVE;
    }

    public static User of(String loginId, String password, String name, String profileImgUrl, String email, ProviderType providerType, RoleType role) {
        return new User(loginId, password, name, profileImgUrl, email, providerType, role);
    }

    public void changeUsername(String name) {
        this.name = name;
    }

    public void changeProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = User.this.profileImgUrl;
    }
}
