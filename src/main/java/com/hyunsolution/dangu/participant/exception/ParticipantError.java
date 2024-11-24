package com.hyunsolution.dangu.participant.exception;

import com.hyunsolution.dangu.common.exception.BaseErrorCode;
import com.hyunsolution.dangu.common.exception.ExceptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ParticipantError implements BaseErrorCode {
    PARTICIPANT_NOT_FOUND("PARTICIPANT_400_1", HttpStatus.BAD_REQUEST, "해당 채팅방 참여자를 찾을 수 없습니다."),
    ALREADY_MATCHED("PARTICIPANT_400_2", HttpStatus.BAD_REQUEST, "이미 매칭된 경우 수락/거절이 불가능합니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ExceptionDto getErrorReason() {
        return ExceptionDto.builder().code(code).message(message).httpStatus(httpStatus).build();
    }
}
