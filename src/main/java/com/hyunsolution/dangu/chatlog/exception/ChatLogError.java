package com.hyunsolution.dangu.chatlog.exception;

import com.hyunsolution.dangu.common.exception.BaseErrorCode;
import com.hyunsolution.dangu.common.exception.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatLogError implements BaseErrorCode {
    CHAT_LOG_NOT_FOUND("CHAT_LOG_400_1", HttpStatus.BAD_REQUEST, "채팅 기록을 찾을 수 없습니다."),
    ;
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ExceptionDto getErrorReason() {
        return ExceptionDto.builder().code(code).message(message).httpStatus(httpStatus).build();
    }
}
