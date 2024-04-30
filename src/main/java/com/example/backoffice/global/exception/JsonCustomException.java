package com.example.backoffice.global.exception;

import lombok.Getter;

@Getter
public class JsonCustomException extends CustomException {
    public JsonCustomException(JsonCustomException e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
