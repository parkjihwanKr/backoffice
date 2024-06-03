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
    MATCHED_LOGIN_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-006", "해당 멤버는 자기 자신입니다."),
    EXISTS_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-007", "이미 존재하는 멤버입니다."),
    MATCHED_MEMBER_INFO_ADDRESS(HttpStatus.BAD_REQUEST, "MEMBER-008", "이미 존재하는 멤버 주소 정보입니다."),
    MATCHED_MEMBER_INFO_CONTACT(HttpStatus.BAD_REQUEST, "MEMBER-009", "이미 존재하는 멤버 연락처 정보입니다."),
    MATCHED_MEMBER_INFO_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER-010", "이미 존재하는 멤버 이메일 정보입니다."),
    MATCHED_MEMBER_INFO_MEMBER_NAME(HttpStatus.BAD_REQUEST, "MEMBER-011", "이미 존재하는 멤버 이름 정보입니다."),
    INVALID_MEMBER_IDS(HttpStatus.BAD_REQUEST, "MEMBER-012", "유효하지 않은 멤버 아이디 리스트입니다."),
    NOT_MATCHED_INFO(HttpStatus.BAD_REQUEST, "MEMBER-013", "해당 멤버 정보와 일치하지 않습니다."),
    RESTRICTED_ACCESS_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-014", "해당 멤버는 접근 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
