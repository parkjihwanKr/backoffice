package com.example.backoffice.global.common;

import java.time.format.DateTimeFormatter;

public class DateTimeFormatters {
    // 공통으로 사용할 DateTimeFormatter 정의
    public static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter TIME_FORMATTER
            = DateTimeFormatter.ofPattern("HH:mm:ss");

    // 다른 포맷터를 추가할 수 있습니다.
}
