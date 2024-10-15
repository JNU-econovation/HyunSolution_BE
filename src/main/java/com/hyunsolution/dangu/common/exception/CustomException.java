package com.hyunsolution.dangu.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public ExceptionDto getErrorReason() {
        return errorCode.getErrorReason();
    }
}
