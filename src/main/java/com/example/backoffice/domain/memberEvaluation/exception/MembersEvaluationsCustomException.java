package com.example.backoffice.domain.memberEvaluation.exception;

import com.example.backoffice.global.exception.CustomException;

public class MembersEvaluationsCustomException extends CustomException {

    public MembersEvaluationsCustomException(MembersEvaluationsExceptionCode e){
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
