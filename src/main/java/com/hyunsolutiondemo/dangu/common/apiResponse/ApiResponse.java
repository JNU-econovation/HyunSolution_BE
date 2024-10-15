package com.hyunsolutiondemo.dangu.common.apiResponse;
import com.hyunsolutiondemo.dangu.common.apiResponse.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
public record ApiResponse<T>(

    @JsonIgnore
    HttpStatus httpStatus,
    boolean success,
    @Nullable T response,
    @Nullable ExceptionDto error

){
    public static <T> ApiResponse<T> success(@Nullable final T response) {
        return new ApiResponse<>(HttpStatus.OK, true, response,null);
    }

    public static <T> ApiResponse<T> fail(final CustomException e) {
        return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
    }

}
