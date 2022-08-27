package com.study.spadeworker.domain.auth.jwt.token;

import com.study.spadeworker.domain.auth.jwt.token.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByUserId(String userId);

    UserRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
