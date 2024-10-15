package com.hyunsolution.dangu.user.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class UserNotFoundException extends CustomException {
    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(UserError.USER_NOT_FOUND);
    }
}
