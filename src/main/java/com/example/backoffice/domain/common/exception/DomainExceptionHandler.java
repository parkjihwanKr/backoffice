package com.example.backoffice.domain.common.exception;

import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.reaction.exception.ReactionsCustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "DomainExceptionHandler")
public class DomainExceptionHandler {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
        private String errorCode;
    }

    // Reactions 관련 예외 처리
    @ExceptionHandler(ReactionsCustomException.class)
    public ResponseEntity<ErrorResponse> handleReactionsCustomException(ReactionsCustomException ex) {
        ErrorResponse errorResponse
                = new ErrorResponse(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Boards 관련 예외 처리
    @ExceptionHandler(BoardsCustomException.class)
    public ResponseEntity<ErrorResponse> handleBoardsCustomException(BoardsCustomException ex) {
        ErrorResponse errorResponse
                = new ErrorResponse(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Members 관련 예외 처리
    @ExceptionHandler(MembersCustomException.class)
    public ResponseEntity<ErrorResponse> handleMembersCustomException(MembersCustomException ex) {
        ErrorResponse errorResponse
                = new ErrorResponse(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 기타 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Error Message Cause : "+ex.getCause());
        log.error("Error Message : "+ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("서버에서 예외가 발생했습니다.", "GENERAL_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
