package com.example.backoffice.domain.answer.exception;

import com.example.backoffice.global.exception.CustomException;

public class AnswersCustomException extends CustomException {

    public AnswersCustomException(AnswersExceptionCode e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
