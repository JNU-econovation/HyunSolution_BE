package com.hyunsolution.dangu.user.service;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.dto.response.LoginResponse;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.user.exception.UserWrongPasswordException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse login(String uid, String password) {
        return userRepository.findByUid(uid)
                .map(user ->loginIfMatchPassword(user,password) ) //유저 존재시 비번 체크&로그인 처리
                .orElse(registerAndLogin(uid,password)); //유저가 없으면 회원가입&로그인 처리
    }

    private LoginResponse registerAndLogin(String uid, String password) {
        User newUser = registerUser(uid, password);
        return new LoginResponse(newUser.getId());
    }

    private LoginResponse loginIfMatchPassword(User user, String password) {
        if (isMatchPassword(password, user)) {
            return new LoginResponse(user.getId());
        } else {
            throw UserWrongPasswordException.USER_WRONG_PASSWORD_EXCEPTION;
        }
    }

    public User registerUser(String uid, String password) {
        return userRepository.save(
                User.builder().uid(uid).password(passwordEncoder.encode(password)).build());
    }

    private boolean isMatchPassword(String rawPwd, User loginUser) {
        return passwordEncoder.matches(rawPwd, loginUser.getPassword());
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
