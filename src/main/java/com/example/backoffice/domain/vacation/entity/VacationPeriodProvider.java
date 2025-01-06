package com.example.backoffice.domain.vacation.entity;

import com.example.backoffice.global.date.DateTimeUtils;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@Component
public class VacationPeriodProvider {

    private static final String VACATION_PERIOD = "VacationPeriod:";
    private static final String VACATION_PERIOD_KEY_FORMAT = VACATION_PERIOD + "%d:%02d";
    private static final String VACATION_PERIOD_VALUE_FORMAT = "%d:%02d";

    private VacationPeriod vacationPeriod; // VacationPeriod를 저장하는 변수

    public void setVacationPeriod(
            LocalDateTime startDate, LocalDateTime endDate) {
        this.vacationPeriod = VacationPeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public String createKey(Long year, Long month) {
        return String.format(VACATION_PERIOD_KEY_FORMAT, year, month);
    }

    public String createValues(int startDay, int endDay) {
        return String.format(VACATION_PERIOD_VALUE_FORMAT, startDay, endDay);
    }

    public LocalDateTime calculateUpcomingStartDate(String key, String value){
        long savedYear = getYearByKey(key);
        long savedMonth = getMonthByKey(key);
        long startDay = getStartDayByValue(value);

        return DateTimeUtils.of(savedYear, savedMonth, startDay);
    }

    public LocalDateTime calculateUpcomingEndDate(String key, String value){
        long savedYear = getYearByKey(key);
        long savedMonth = getMonthByKey(key);
        long endDay = getEndDayByValue(value);

        return DateTimeUtils.of(savedYear, savedMonth, endDay);
    }

    private Long getYearByKey(String key){
        String removedPrefixKey = removeKeyPrefix(key);
        return Long.parseLong(removedPrefixKey.substring(0, 4));
    }

    private Long getMonthByKey(String key){
        String removedPrefixKey = removeKeyPrefix(key);
        return Long.parseLong(removedPrefixKey.substring(5, 7));
    }

    private String removeKeyPrefix(String key){
        if (StringUtils.hasText(key) && key.startsWith(VACATION_PERIOD)) {
            return key.substring(15);
        }
        return null;
    }

    private Long getStartDayByValue(String value) {
        if (StringUtils.hasText(value) && value.contains(":")) {
            return Long.parseLong(value.split(":")[0]);
        }
        return null;
    }

    private Long getEndDayByValue(String value) {
        if (StringUtils.hasText(value) && value.contains(":")) {
            return Long.parseLong(value.split(":")[1]);
        }
        return null;
    }
}
