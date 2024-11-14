package com.hyunsolution.dangu.chatting.exception;

import com.hyunsolution.dangu.common.exception.BaseErrorCode;
import com.hyunsolution.dangu.common.exception.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatError implements BaseErrorCode {
    CHATROOM_NOT_FOUND("CHAT_400", HttpStatus.BAD_REQUEST, "존재하지 않는 채팅방입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ExceptionDto getErrorReason(){
        return ExceptionDto.builder().code(code).message(message).httpStatus(httpStatus).build();
    }


}
