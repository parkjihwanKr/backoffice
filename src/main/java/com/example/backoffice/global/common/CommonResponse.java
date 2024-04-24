package com.example.backoffice.global.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class CommonResponse<T> {

    private Integer httpStatus;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public CommonResponse(HttpStatus httpStatus, String message, T data) {
        this(httpStatus.value(), message, data);
    }

    public CommonResponse(HttpStatus httpStatus, String message) {
        this(httpStatus.value(), message, null);
    }

    public CommonResponse(T paymentKey) {
    }
}
