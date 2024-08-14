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
    MATCHED_BEFORE_NUMBER_AND_AFTER_NUMBER(HttpStatus.BAD_REQUEST, "QUESTIONS-005", "똑같은 숫자로 바꿀 수 없습니다."),
    NOT_EXCEED_TWENTY_OR_BELOW_ZERO(HttpStatus.BAD_REQUEST, "QUESTIONS-006", "요청하는 순서는 0이상 20이하여야합니다."),
    NOT_FOUND_QUESTIONS_ORDER(HttpStatus.BAD_REQUEST,"QUESTIONS-007", "해당 질문의 순서를 잘못 입력하셨습니다."),
    NEED_EXCEED_ONE_ANSWER_OR_NULL(HttpStatus.BAD_REQUEST, "QUESTIONS-009", "해당 질문에 대한 답은 1개 초과거나 없어야합니다."),
    MATCHED_BEFORE_AND_AFTER_ORDER(HttpStatus.BAD_REQUEST, "QUESTIONS-010", "질문의 바꾸려는 순서가 전과 후가 같으면 바뀌지 않습니다."),
    ORDER_EXCEEDS_MAX_SIZE(HttpStatus.BAD_REQUEST, "QUESTIONS-011","질문 리스트의 순서가 최대 크기를 초과했습니다."),
    NOT_INPUT_DELETED_QUESTION(HttpStatus.BAD_REQUEST, "QUESTIONS-012", "삭제할 질문을 입력하지 않았습니다."),
    EXIST_EVALUATION_QUESTION_LIST(HttpStatus.BAD_REQUEST, "QUESTIONS-013", "해당 설문조사에 질문 리스트가 존재합니다."),
    INPUT_MULTIPLE_CHOICE_ANSWER(HttpStatus.BAD_REQUEST, "QUESTIONS-014","객관식으로만 입력해주세요."),
    INPUT_SHORT_ANSWER(HttpStatus.BAD_REQUEST, "QUESTIONS-014","주관식으로만 입력해주세요."),
    CHECKED_IS_FALSE(HttpStatus.BAD_REQUEST, "QUESTIONS-015","설문조사를 자기 자신이 만들었다는 동의 부분에 체크를 해야 진행됩니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
