package com.study.spadeworker.domain.auth.jwt.token;

import com.study.spadeworker.global.config.audit.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserRefreshToken extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, unique = true)
    private String userId;

    @Column(length = 256)
    private String refreshToken;

    public UserRefreshToken(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
