package com.example.backoffice.domain.question.exception;

import com.example.backoffice.global.exception.CustomException;

public class QuestionsCustomException extends CustomException {

    public QuestionsCustomException(QuestionsExceptionCode e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
