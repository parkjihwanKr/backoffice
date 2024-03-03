package com.example.backoffice.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T> {

    private T data;
    private String message;
    private Integer httpStatusCode;

    public CommonResponseDto(T data, String message){
        this.data = data;
        this.message = message;
    }
}
