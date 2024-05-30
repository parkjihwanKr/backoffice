package com.example.backoffice.domain.notification.exception;

import com.example.backoffice.global.exception.CustomException;

public class NotificationsCustomException extends CustomException {
    public NotificationsCustomException(NotificationsExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
