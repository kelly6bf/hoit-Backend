package com.study.spadeworker.domain.user.entity;

import com.study.spadeworker.global.config.audit.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserProfile extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(length = 512)
    private String profileImgUrl;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String email;

}
