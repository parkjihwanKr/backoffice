package com.example.backoffice.domain.favorite.exception;

import com.example.backoffice.global.exception.CustomException;

public class FavoritiesCustomException extends CustomException {
    public FavoritiesCustomException(FavoritiesExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
