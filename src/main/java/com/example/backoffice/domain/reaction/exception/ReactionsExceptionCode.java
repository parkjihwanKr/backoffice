package com.example.backoffice.domain.reaction.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReactionsExceptionCode {
    NOT_MATCHED_EMOJI(HttpStatus.BAD_REQUEST, "REACTION-001", "해당 이모지의 요청이 잘못되었습니다."),
    NOT_FOUND_REACTION(HttpStatus.BAD_REQUEST, "REACTION-002", "해당 반응을 찾을 수 없습니다."),
    EMOJI_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "REACTION-003", "이미 이모지가 존재합니다."),
    NOT_ACCEPTED_EMOJI(HttpStatus.BAD_REQUEST, "REACTION-004", "허용되지 않는 이모지입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
