package com.example.backoffice.domain.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationExceptionCode {
    NOT_FOUND_NOTIFICATION(HttpStatus.BAD_REQUEST, "NOTIFICATION-001", "해당 알림을 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
