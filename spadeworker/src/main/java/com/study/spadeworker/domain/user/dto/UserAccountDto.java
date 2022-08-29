package com.study.spadeworker.domain.user.dto;

import com.study.spadeworker.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserAccountDto {
    private Long id;

    private String username;

    private String profileImgUrl;

    private UserAccountDto(Long id, String username, String profileImgUrl) {
        this.id = id;
        this.username = username;
        this.profileImgUrl = profileImgUrl;
    }

    public static UserAccountDto from(User user) {
        return new UserAccountDto(user.getId(), user.getName(), user.getProfileImgUrl());
    }
}
