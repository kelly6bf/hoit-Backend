package com.study.spadeworker.domain.user.service;

import com.study.spadeworker.domain.user.dto.UserAccountDto;
import com.study.spadeworker.domain.user.entity.User;
import com.study.spadeworker.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLoginId(authentication.getName());
    }

    public UserAccountDto getUserAccountDto(User user) {
        return (user != null) ? UserAccountDto.from(user) : null;
    }
}
