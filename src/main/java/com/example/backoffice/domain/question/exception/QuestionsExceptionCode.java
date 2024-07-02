package com.example.backoffice.domain.question.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuestionsExceptionCode {

    NOT_FOUND_QUESTION(HttpStatus.BAD_REQUEST,"QUESTIONS-001","해당 질문을 찾을 수 없습니다."),
    NOT_PERMISSION_DEPARTMENT_OR_POSITION(HttpStatus.BAD_REQUEST, "QUESTIONS-002","해당 질문을 만들 수 있는 부서나 직위가 아닙니다."),
    NOT_FOUND_QUESTION_TYPE(HttpStatus.BAD_REQUEST,"QUESTIONS-003","해당 질문 타입을 찾을 수 없습니다."),
    NOT_EMPTY_ANSWER(HttpStatus.BAD_REQUEST, "QUESTIONS-004", "해당 질문에 대한 답은 공란일 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
