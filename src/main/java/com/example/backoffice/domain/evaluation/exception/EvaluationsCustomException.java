package com.example.backoffice.domain.evaluation.exception;

import com.example.backoffice.global.exception.CustomException;

public class EvaluationsCustomException extends CustomException {

    public EvaluationsCustomException(EvaluationsExceptionCode e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
