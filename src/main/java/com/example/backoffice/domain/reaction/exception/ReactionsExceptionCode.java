package com.example.backoffice.domain.reaction.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReactionsExceptionCode {
    NOT_MATCHED_EMOJI(HttpStatus.BAD_REQUEST, "REACTION-001", "해당 이모지의 요청이 잘못되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
