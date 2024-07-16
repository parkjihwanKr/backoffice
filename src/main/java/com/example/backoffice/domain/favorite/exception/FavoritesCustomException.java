package com.example.backoffice.domain.favorite.exception;

import com.example.backoffice.global.exception.CustomException;

public class FavoritesCustomException extends CustomException {
    public FavoritesCustomException(FavoritesExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
