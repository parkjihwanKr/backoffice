package com.example.backoffice.domain.attendance.exception;

import com.example.backoffice.global.exception.CustomException;

public class AttendancesCustomException extends CustomException {
    public AttendancesCustomException(AttendancesExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
