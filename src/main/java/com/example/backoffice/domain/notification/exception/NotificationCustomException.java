package com.example.backoffice.domain.notification.exception;

import com.example.backoffice.global.exception.CustomException;

public class NotificationCustomException extends CustomException {
    public NotificationCustomException(NotificationExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
