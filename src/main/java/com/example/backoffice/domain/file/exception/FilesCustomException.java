package com.example.backoffice.domain.file.exception;

import com.example.backoffice.global.exception.CustomException;

public class FilesCustomException extends CustomException {

    public FilesCustomException(FilesExceptionCode e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
