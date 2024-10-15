package com.hyunsolutiondemo.dangu.common.apiResponse;

import lombok.Getter;
import com.hyunsolutiondemo.dangu.common.apiResponse.ErrorCode;
import javax.validation.constraints.NotNull;

@Getter
public class ExceptionDto {
    @NotNull
    private final Integer code;

    @NotNull
    private final String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.code=errorCode.getCode();
        this.message=errorCode.getMessage();
    }

    public static ExceptionDto of(ErrorCode errorCode){

        return new ExceptionDto(errorCode);
    }


}
