package com.example.backoffice.domain.answer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AnswersExceptionCode {
    NOT_FOUND_ANSWER(HttpStatus.BAD_REQUEST,"ANSWERS-001", "해당 답변을 찾을 수 없습니다."),
    NEED_MULTIPLE_CHOICE_ANSWER(HttpStatus.BAD_REQUEST, "ANSWERS-002", "객관식 질문에 알맞은 형태로 작성해주세요."),
    NEED_SHORT_ANSWER(HttpStatus.BAD_REQUEST, "ANSWERS-003","주관식 질문에 알맞는 형태로 작성해주세요"),
    NOT_FOUND_QUESTIONS_TYPE(HttpStatus.BAD_REQUEST, "ANSWERS-004", "답에 대한 질문 타입이 존재하지 않습니다."),
    NOT_FOUND_ANSWER_LIST_BY_QUESTION_ID_OR_NEED_CHOICE_SHORT_ANSWER(
            HttpStatus.BAD_REQUEST, "ANSWER-005", "해당 질문에 답을 찾을 수 없거나 해당 질문 타입을 주관식으로 만드셔야합니다."),
    NOT_FOUND_ANSWER_LIST_BY_QUESTION_ID(HttpStatus.BAD_REQUEST, "ANSWERS-006", "해당 질문에 대한 답을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
