package com.example.backoffice.global.exception;

import com.example.backoffice.domain.attendance.exception.AttendancesCustomException;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.reaction.exception.ReactionsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.global.dto.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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


    /*@ExceptionHandler(VacationsCustomException.class)
    public ResponseEntity<CommonResponseDto<String>> handleVacationsException(VacationsCustomException ex) {
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                ex.getErrorCode(),  // 에러 코드
                ex.getMessage(),    // 에러 메시지
                ex.getHttpStatus().value()  // 상태 코드 설정
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }*/

    /* 그 외의 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDto<Void>> handleGlobalException(Exception ex) {
        // 에러 응답 생성
        CommonResponseDto<Void> errorResponse = new CommonResponseDto<>(
                null,  // 데이터는 null로 설정
                ex.getMessage(),  // 예외 메시지
                HttpStatus.INTERNAL_SERVER_ERROR.value()  // 500 상태 코드
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);  // 상태 코드로 응답
    }

    // 그 외의 일반적인 예외 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CommonResponseDto<String>> handleNullPointerException(NullPointerException ex) {
        // 에러 응답 생성
        CommonResponseDto<String> errorResponse = new CommonResponseDto<>(
                "NULL-001",  // 데이터는 null로 설정
                "입력칸을 채워주세요!",  // 예외 메시지
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);  // 상태 코드로 응답
    }*/
}
