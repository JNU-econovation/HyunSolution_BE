package com.hyunsolution.dangu.common.exception;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ExceptionDto {
    @NotNull private final String code;
    @NotNull private final String message;
    @NotNull private final HttpStatus httpStatus;
}
