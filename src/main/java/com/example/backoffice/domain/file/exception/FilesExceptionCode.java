package com.example.backoffice.domain.file.exception;

import com.example.backoffice.global.exception.CustomException;

public class FilesExceptionCode extends CustomException {

    public FilesExceptionCode(CustomException e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
