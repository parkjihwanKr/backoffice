package com.example.backoffice.global.date;

import com.example.backoffice.global.exception.DateUtilException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Getter
    private static final LocalDateTime todayCheckInTime =
            LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
    @Getter
    private static final LocalDateTime todayCheckOutTime =
            LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0));

    // 캐싱 데이터
    private static LocalDateTime today;
    private static LocalDateTime tomorrow;

    // 현재 시점(LocalDateTime) 반환
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // 특정 문자열을 LocalDateTime으로 파싱
    public static LocalDateTime parse(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new DateUtilException(GlobalExceptionCode.NOT_PARSE_DATE);
        }
    }

    public static LocalDateTime getToday(){
        if(today == null){
            today = LocalDate.now().atStartOfDay();
        }
        return today;
    }

    // 내일의 시작 시각을 반환
    public static LocalDateTime getTomorrow() {
        LocalDateTime today = getToday();
        if (tomorrow == null || !tomorrow.toLocalDate().isEqual(today.toLocalDate())) {
            tomorrow = today.plusDays(1);
        }
        return tomorrow;
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
                throw new DateUtilException(
                        GlobalExceptionCode.START_DATE_AFTER_END_DATE);
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

    public static Boolean isWithinHours(LocalDateTime checkInTime, LocalDateTime checkOutTime, Integer hours){
        if (Duration.between(checkInTime, checkOutTime).toHours() <= hours) {
            return false;
        }
        return true;
    }

    public static boolean isBeforeTodayCheckOutTime(LocalDateTime time) {
        return time.isBefore(getTodayCheckOutTime());
    }

    public static boolean isWeekday() {
        LocalDate today = getToday().toLocalDate();
        return today.getDayOfWeek().getValue() >= 1 && today.getDayOfWeek().getValue() <= 5;
    }

    // 작년과
    public static void matchedBeforeOneYearOrThisYear(Long year){
        if(!(year > today.getYear() -1 && year == today.getYear())){
            throw new DateUtilException(GlobalExceptionCode.NOT_RETRIEVE_DATA);
        }
    }

    public static void validateMonth(Long month){
        if(month <= 0 || month >= 13){
            throw new DateUtilException(GlobalExceptionCode.NOT_EXIST_MONTH);
        }
    }
}
