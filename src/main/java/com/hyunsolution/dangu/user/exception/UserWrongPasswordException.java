package com.hyunsolution.dangu.user.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class UserWrongPasswordException extends CustomException {
    public static final UserWrongPasswordException USER_WRONG_PASSWORD_EXCEPTION =
            new UserWrongPasswordException();

    private UserWrongPasswordException() {
        super(UserError.USER_WRONG_PASSWORD);
    }
}
