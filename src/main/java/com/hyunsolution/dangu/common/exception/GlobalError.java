package com.hyunsolution.dangu.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalError implements BaseErrorCode {
    // Test error
    TEST_ERROR("TEST_400_1", HttpStatus.BAD_REQUEST, "테스트 에러입니다."),

    // 404
    NOT_FOUND_END_POINT("GLOBAL_404_1", HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),

    // 500
    INTERNAL_SERVER_ERROR("GLOBAL_500_1", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ExceptionDto getErrorReason() {
        return null;
    }
}
