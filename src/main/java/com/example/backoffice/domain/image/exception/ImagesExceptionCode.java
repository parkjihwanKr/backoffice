package com.example.backoffice.domain.image.exception;

import com.example.backoffice.global.exception.CustomException;

public class ImagesExceptionCode extends CustomException {

    public ImagesExceptionCode(CustomException e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
