package com.study.spadeworker.domain.user.controller;

import com.study.spadeworker.domain.user.dto.UserAccountDto;
import com.study.spadeworker.domain.user.repository.UserRepository;
import com.study.spadeworker.domain.user.service.UserService;
import com.study.spadeworker.global.response.ResponseService;
import com.study.spadeworker.global.response.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    /**
     * 사용자 정보를 반환하는 api
     */
    @GetMapping("/user-info")
    public SingleResult<UserAccountDto> getUserInfo() {
        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 조회하였습니다.",
                userService.getUserAccountDto(
                        userService.getCurrentUser()
                )
        );
    }
}
