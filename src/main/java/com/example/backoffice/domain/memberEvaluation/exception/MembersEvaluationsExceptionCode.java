package com.example.backoffice.domain.memberEvaluation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembersEvaluationsExceptionCode {
    NOT_FOUND_MEMBERS_EVALUATIONS(HttpStatus.BAD_REQUEST, "MEMBERS-EVALUATIONS-001","해당 멤버에 대한 설문조사는 존재하지 않습니다."),
    NOT_COMPLETED_EVALUATION(HttpStatus.BAD_REQUEST,"MEMBERS-EVALUATIONS-002","해당 설문조사를 응하지 않았습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
