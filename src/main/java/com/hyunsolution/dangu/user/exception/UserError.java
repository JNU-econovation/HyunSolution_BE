package com.hyunsolution.dangu.user.exception;

import com.hyunsolution.dangu.common.exception.BaseErrorCode;
import com.hyunsolution.dangu.common.exception.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserError implements BaseErrorCode {
    USER_NOT_FOUND("USER_400_1", HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ExceptionDto getErrorReason() {
        return ExceptionDto.builder().code(code).message(message).httpStatus(httpStatus).build();
    }
}
