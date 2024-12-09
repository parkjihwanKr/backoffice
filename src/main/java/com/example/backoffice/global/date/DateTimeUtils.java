package com.example.backoffice.global.date;

import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.exception.DateUtilException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.example.backoffice.global.common.DateTimeFormatters.DATE_FORMATTER;

public class DateTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Getter
    private static final LocalDateTime todayCheckInTime =
            LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
    @Getter
    private static final LocalDateTime todayCheckOutTime =
            LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0));

    // 하루마다 갱신되는 캐싱 데이터
    private static LocalDateTime today;
    private static LocalDateTime tomorrow;

    // 0초 접미사 상수
    public static final String suffixZeroSeconds = ":00";

    // 현재 시점(LocalDateTime) 반환
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static LocalTime getCheckInTime(){
        return todayCheckInTime.toLocalTime();
    }

    public static LocalTime getCheckOutTime(){
        return todayCheckOutTime.toLocalTime();
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

    // 오늘이 지나서 오늘, 내일을 변경해야함
    public static void refreshCached() {
        today = LocalDate.now().atStartOfDay();
        tomorrow = today.plusDays(1);
    }

    // 특정 년도와 월의 시작일을 반환
    public static LocalDateTime getStartDayOfMonth(Long year, Long month) {
        validateYearAndMonth(year, month);
        return LocalDateTime.of(year.intValue(), month.intValue(), 1, 0, 0);
    }

    // 특정 년도와 월의 마지막 날을 반환
    public static LocalDateTime getEndDayOfMonth(Long year, Long month) {
        validateYearAndMonth(year, month);
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


    public static Boolean isWithinHours(LocalDateTime checkInTime, LocalDateTime checkOutTime, Integer hours){
        if (Duration.between(checkInTime, checkOutTime).toHours() <= hours) {
            return false;
        }
        return true;
    }

    public static boolean isBeforeTodayCheckOutTime(LocalDateTime time) {
        return time.isBefore(getTodayCheckOutTime());
    }

    public static boolean isBetweenTodayCheckOutTime(LocalDateTime checkOutTime) {
        LocalDateTime startRange = getTodayCheckOutTime().minusMinutes(30);
        LocalDateTime endRange = getTodayCheckOutTime().plusHours(1);

        // checkOutTime이 범위 내에 있는지 확인
        return !checkOutTime.isBefore(startRange) && !checkOutTime.isAfter(endRange);
    }

    public static boolean isWeekday() {
        LocalDate today = getToday().toLocalDate();
        return today.getDayOfWeek().getValue() >= 1 && today.getDayOfWeek().getValue() <= 5;
    }

    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new DateUtilException(GlobalExceptionCode.NOT_PARSE_DATE);
        }
        LocalDate todayDate = getToday().toLocalDate();
        return dateTime.toLocalDate().isEqual(todayDate);
    }

    public static boolean isBeforeToday(LocalDateTime dateTime) {
        return dateTime.isBefore(getToday()) ? true : false;
    }

    public static boolean isAfterToday(LocalDateTime dateTime) {
        return dateTime.isAfter(getTomorrow()) ? true : false;
    }

    public static Long calculateMinutesFromTodayToEndDate(LocalDateTime endDate){
        Duration duration = Duration.between(today, endDate);
        return duration.toMinutes();
    }

    public static LocalDateTime of(Long year, Long month, Long day){
        validateYearAndMonth(year, month);
        return LocalDateTime.of(
                year.intValue(), month.intValue(), day.intValue(), 0, 0, 0);
    }

    public static boolean isInDateRange(DateRange dateRange) {
        return (today.isEqual(dateRange.getStartDate()) || today.isAfter(dateRange.getStartDate()))
                && (today.isBefore(dateRange.getEndDate()) || today.isEqual(dateRange.getEndDate()));
    }

    private static void validateYearAndMonth(Long year, Long month){
        if (year < 1) {
            throw new DateUtilException(GlobalExceptionCode.INVALID_YEAR);
        }

        if (month < 1 || month > 12) {
            throw new DateUtilException(GlobalExceptionCode.INVALID_MONTH);
        }
    }

    public static boolean isHoliday(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new DateUtilException(GlobalExceptionCode.NOT_PARSE_DATE);
        }

        // LocalDate로 변환
        LocalDate date = dateTime.toLocalDate();

        // 주말(토요일/일요일)인지 확인
        int dayOfWeek = date.getDayOfWeek().getValue(); // 월(1) ~ 일(7)
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            return true; // 토요일 또는 일요일
        }

        return false; // 평일
    }
}
