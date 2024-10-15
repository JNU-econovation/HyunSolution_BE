package com.hyunsolution.dangu.common.apiResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public String getMessage() {
        return errorCode.getMessage();
    }
}
