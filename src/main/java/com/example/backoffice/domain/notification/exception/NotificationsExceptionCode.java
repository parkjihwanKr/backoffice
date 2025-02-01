package com.example.backoffice.domain.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationsExceptionCode {
    NOT_FOUND_NOTIFICATION(HttpStatus.BAD_REQUEST, "NOTIFICATION-001", "해당 알림을 찾을 수 없습니다."),
    NOT_MATCHED_NOTIFICATION_TYPE(HttpStatus.BAD_REQUEST,"NOTIFICATION-004", "해당 알림의 타입은 존재하지 않습니다."),
    RESTRICT_ACCESS(HttpStatus.FORBIDDEN, "NOTIFICATION-005", "해당 유저는 접근 할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
