package com.example.backoffice.global.exception;

import com.example.backoffice.domain.answer.exception.AnswersCustomException;
import com.example.backoffice.domain.attendance.exception.AttendancesCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.favorite.exception.FavoritesCustomException;
import com.example.backoffice.domain.file.exception.FilesCustomException;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.memberEvaluation.exception.MembersEvaluationsCustomException;
import com.example.backoffice.domain.notification.exception.NotificationsCustomException;
import com.example.backoffice.domain.question.exception.QuestionsCustomException;
import com.example.backoffice.domain.reaction.exception.ReactionsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.global.dto.CommonResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 멤버 관련 커스텀 예외 처리
    @ExceptionHandler(MembersCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleMemberException(MembersCustomException ex) {
        // 에러 응답 생성
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),
                ex.getMessage(),  // 커스텀 예외에서 전달된 메시지
                ex.getHttpStatus().value() // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());  // 상태 코드로 응답
    }

    // 휴가 관련 커스텀 예외 처리
    @ExceptionHandler(VacationsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleVacationException(VacationsCustomException ex) {
        // 에러 응답 생성
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 데이터는 null로 설정
                ex.getMessage(),  // 커스텀 예외에서 전달된 메시지
                ex.getHttpStatus().value() // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());  // 상태 코드로 응답
    }

    // JWT TOKEN 관련 커스텀 예외 처리
    @ExceptionHandler(JwtCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleJwtException(JwtCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    // 근태 관련 커스텀 예외 처리
    @ExceptionHandler(AttendancesCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleAttendanceException(AttendancesCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    // DateUtils 관련 커스텀 예외 처리
    @ExceptionHandler(DateUtilException.class)
    public ResponseEntity<CommonResponseDto<String>> handleDateException(DateUtilException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(EventsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleEventsException(EventsCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ReactionsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleReactionsException(ReactionsCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(FilesCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleFilesException(
            FilesCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(AnswersCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleAnswersException(
            AnswersCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(EvaluationsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleEvaluationsException(
            EvaluationsCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(MembersEvaluationsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleMembersEvaluationsException(
            MembersEvaluationsCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(QuestionsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleQuestionsException(
            QuestionsCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(FavoritesCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleFavoritesException(
            FavoritesCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(NotificationsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleNotificationsException(
            NotificationsCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponseDto<String>> handleConstraintViolationException(
            ConstraintViolationException ex) {

        // 에러 메시지를 수집
        Map<String, String> errorMap = new HashMap<>();
        final String ERROR_KEY = "AUTH-ERROR";

        // ConstraintViolation의 메시지를 스트림으로 처리하여 병합
        String errorMessages = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("Validation error occurred.");

        errorMap.put(ERROR_KEY, errorMessages);

        // CommonResponseDto를 생성
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ERROR_KEY,
                errorMessages,
                HttpStatus.BAD_REQUEST.value()
        );

        // ResponseEntity로 반환
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
