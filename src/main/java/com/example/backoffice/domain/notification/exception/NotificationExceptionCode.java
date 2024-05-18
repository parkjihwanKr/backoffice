package com.example.backoffice.domain.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationExceptionCode {
    NOT_XXX_XXX(HttpStatus.BAD_REQUEST, "NOTIFICATION-001", "XXX."),

    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
