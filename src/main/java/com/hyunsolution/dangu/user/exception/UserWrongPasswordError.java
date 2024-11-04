package com.hyunsolution.dangu.user.exception;

import com.hyunsolution.dangu.common.exception.BaseErrorCode;
import com.hyunsolution.dangu.common.exception.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserWrongPasswordError implements BaseErrorCode {
    USER_WRONG_PASSWORD("USER_400_2", HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ExceptionDto getErrorReason() {
        return ExceptionDto.builder().code(code).message(message).httpStatus(httpStatus).build();
    }
}
