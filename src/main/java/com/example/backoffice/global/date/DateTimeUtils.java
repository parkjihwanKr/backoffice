package com.example.backoffice.global.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // 현재 시점(LocalDateTime) 반환
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // 특정 문자열을 LocalDateTime으로 파싱
    public static LocalDateTime parse(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    }

    public static LocalDateTime getEndOfDay(){
        return LocalDate.now()
                .plusDays(1)
                .atStartOfDay()
                .minusSeconds(1);
    }

    public static LocalDateTime getTomorrow(){
        return LocalDate.now().plusDays(1).atStartOfDay();
    }

    public static LocalDateTime getStartDayOfMonth(Long year, Long month){
        return LocalDateTime.of(year.intValue(), month.intValue(), 1, 0, 0);
    }

    public static LocalDateTime getEndDayOfMonth(Long year, Long month){
        return getStartDayOfMonth(year, month).plusMonths(1).minusSeconds(1);
    }
}
