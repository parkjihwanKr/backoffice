package com.example.backoffice.global.date;

import com.example.backoffice.global.exception.DateCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // 현재 시점(LocalDateTime) 반환
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // 특정 문자열을 LocalDateTime으로 파싱
    public static LocalDateTime parse(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new DateCustomException(GlobalExceptionCode.INVALID_DATE_FORMAT);
        }
    }

    public static LocalDateTime withDayOfMonth(int day){
        return getCurrentDateTime().withDayOfMonth(day);
    }

    public static LocalDateTime findFirstMonday(LocalDateTime firstDayOfMonth){
        return firstDayOfMonth.plusDays(
                (8 - firstDayOfMonth.getDayOfWeek().getValue()) % 7);
    }

    // 하루의 끝 시각을 반환 (23:59:59)
    public static LocalDateTime getEndOfDay() {
        return LocalDate.now()
                .plusDays(1)
                .atStartOfDay()
                .minusSeconds(1);
    }

    // 내일의 시작 시각을 반환
    public static LocalDateTime getTomorrow() {
        return LocalDate.now().plusDays(1).atStartOfDay();
    }

    // 특정 년도와 월의 시작일을 반환
    public static LocalDateTime getStartDayOfMonth(Long year, Long month) {
        return LocalDateTime.of(year.intValue(), month.intValue(), 1, 0, 0);
    }

    // 특정 년도와 월의 마지막 날을 반환
    public static LocalDateTime getEndDayOfMonth(Long year, Long month) {
        return getStartDayOfMonth(year, month).plusMonths(1).minusSeconds(1);
    }

    // 날짜 검증 로직: 시작일과 종료일 검증
    public static void validateStartAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("시작일이 종료일보다 이후일 수 없습니다.");
            }
        }
    }

    // 날짜 문자열을 파싱하고 검증까지 처리
    public static void validateAndParseDates(String startDateStr, String endDateStr) {
        LocalDateTime startDate = startDateStr != null ? parse(startDateStr) : null;
        LocalDateTime endDate = endDateStr != null ? parse(endDateStr) : null;

        // 시작일과 종료일 검증
        validateStartAndEndDate(startDate, endDate);
    }
}
