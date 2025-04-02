package com.example.backoffice.domain.vacation.entity;

import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.global.date.DateTimeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@Component
public class VacationPeriodProvider {

    private static final String PREFIX_VACATION_PERIOD = "Upcoming::VacationPeriod:";
    private static final String VACATION_PERIOD_KEY_FORMAT = PREFIX_VACATION_PERIOD + "%d:%02d";
    private static final String VACATION_PERIOD_VALUE_FORMAT = "%d:%02d";
    private static final String ZERO_PREFIX = "0";

    private VacationPeriod vacationPeriod; // VacationPeriod를 저장하는 변수

    private final ObjectMapper objectMapper;
    public VacationPeriodProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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

    public VacationsResponseDto.ReadPeriodDto createValues(
            LocalDateTime startDate, LocalDateTime endDate) {
        return new VacationsResponseDto.ReadPeriodDto(startDate, endDate);
    }

    public LocalDateTime calculateUpcomingStartDate(String key, String value){
        long savedYear = getYearByKey(key);
        long savedMonth = getMonthByKey(key);
        long startDay = getStartDayByValue(value);

        return DateTimeUtils.of(savedYear, savedMonth, startDay);
    }

    public String calculateUpcomingStartDateToString(String key, String value){
        Long savedYear = getYearByKey(key);
        Long savedMonth = getMonthByKey(key);
        String savedMonthToString = null;

        if(savedMonth < 10){
            savedMonthToString = ZERO_PREFIX+savedMonth;
        }else{
            savedMonthToString = savedMonth.toString();
        }
        Long startDay = getStartDayByValue(value);
        String startDayToString = null;
        if(startDay < 10){
            startDayToString = ZERO_PREFIX+startDay;
        }else{
            startDayToString = startDay.toString();
        }
        return savedYear+"-"+savedMonthToString+"-"+startDayToString+"T00:00:01";
    }

    public LocalDateTime calculateUpcomingEndDate(String key, String value){
        long savedYear = getYearByKey(key);
        long savedMonth = getMonthByKey(key);
        long endDay = getEndDayByValue(value);

        return DateTimeUtils.of(savedYear, savedMonth, endDay);
    }

    public String calculateUpcomingEndDateToString(String key, String value){
        Long savedYear = getYearByKey(key);
        Long savedMonth = getMonthByKey(key);
        Long endDay = getEndDayByValue(value);

        String savedMonthToString = null;

        if(savedMonth < 10){
            savedMonthToString = ZERO_PREFIX+savedMonth;
        }else{
            savedMonthToString = savedMonth.toString();
        }
        String startDayToString = null;
        if(endDay < 10){
            startDayToString = ZERO_PREFIX+endDay;
        }else{
            startDayToString = endDay.toString();
        }

        return savedYear+"-"+savedMonthToString+"-"+startDayToString+"T23:59:59";
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
        if (StringUtils.hasText(key) && key.startsWith(PREFIX_VACATION_PERIOD)) {
            return key.substring(PREFIX_VACATION_PERIOD.length());
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
