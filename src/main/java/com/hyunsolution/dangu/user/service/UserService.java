package com.hyunsolution.dangu.user.service;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.dto.response.LoginResponse;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.user.exception.UserWrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    // TODO: 왜 트랜잭션 어노테이션을 붙이는지 공부하기(with flush가 언제 일어나나) - 다현
    public LoginResponse login(String uid, String password) {
        User loginUser = userRepository.findByUid(uid);
        // 사용자 존재 여부 판단
        if (loginUser == null) {
            User newUser = registerUser(uid, password);
            return new LoginResponse(newUser.getId());
        }
        // 비밀번호 일치 확인
        if (loginUser.getPassword().equals(password)) {
            return new LoginResponse(loginUser.getId());
        } else {
            throw UserWrongPasswordException.USER_WRONG_PASSWORD_EXCEPTION;
        }
    }

    public User registerUser(String uid, String password) {
        return userRepository.save(User.
                builder()
                .uid(uid)
                .password(password)
                .build());
    }
}
