package com.hyunsolution.dangu.user.controller;

import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import com.hyunsolution.dangu.user.dto.request.LoginRequest;
import com.hyunsolution.dangu.user.dto.response.LoginResponse;
import com.hyunsolution.dangu.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/login")
    @Operation(summary = "로그인", description = "기존아이디, 비밀번호의 경우 로그인을 진행하며 신규 id의 경우 회원가입을 진행한다.")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String uid = loginRequest.getUid();
        String password = loginRequest.getPassword();
        LoginResponse id = userService.login(uid, password);
        return ApiResponse.success(id);
    }
}
