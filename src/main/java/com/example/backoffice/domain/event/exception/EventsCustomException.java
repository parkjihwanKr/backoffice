package com.example.backoffice.domain.event.exception;

import com.example.backoffice.global.exception.CustomException;

public class EventsCustomException extends CustomException {
    public EventsCustomException(EventsExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
