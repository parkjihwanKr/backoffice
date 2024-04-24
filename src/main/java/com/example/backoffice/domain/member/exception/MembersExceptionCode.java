package com.example.backoffice.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembersExceptionCode {
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-001", "해당 멤버를 찾을 수 없습니다."),
    NOT_MATCHED_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER-002", "패스워드가 일치하지 않습니다."),
    NOT_MATCHED_MEMBER_NAME(HttpStatus.BAD_REQUEST,"MEMBER-003", "멤버의 이름이 일치하지 않습니다."),
    NOT_BLANK_IMAGE_FILE(HttpStatus.BAD_REQUEST, "MEMBER-004", "멤버가 올린 프로필 이미지 파일은 존재해야합니다."),
    NOT_MATCHED_IMAGE_FILE(HttpStatus.BAD_REQUEST, "MEMBER-005", "멤버가 올린 프로필 이미지 파일의 확장자가 이미지 파일이 아닙니다."),

    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
