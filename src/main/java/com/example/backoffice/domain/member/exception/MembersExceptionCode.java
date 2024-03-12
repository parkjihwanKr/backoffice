package com.example.backoffice.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembersExceptionCode {
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-001", "해당 멤버를 찾을 수 없습니다."),
    NOT_MATCHED_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER-002", "패스워드가 일치하지 않습니다."),
    NOT_MATCHED_MEMBER(HttpStatus.BAD_REQUEST,"MEMBER-003", "멤버가 일치하지 않습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
