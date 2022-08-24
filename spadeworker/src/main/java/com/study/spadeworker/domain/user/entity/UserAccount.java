package com.study.spadeworker.domain.user.entity;

import com.study.spadeworker.domain.user.entity.constant.AccountProviderType;
import com.study.spadeworker.domain.user.entity.constant.AccountRoleType;
import com.study.spadeworker.domain.user.entity.constant.AccountStatus;
import com.study.spadeworker.global.config.audit.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 200, unique = true)
    private String loginId;

    @Column(nullable = false, length = 500)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountProviderType providerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountRoleType role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountStatus status;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
