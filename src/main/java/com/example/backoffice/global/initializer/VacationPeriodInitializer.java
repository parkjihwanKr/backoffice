package com.example.backoffice.global.initializer;

import com.example.backoffice.domain.vacation.entity.VacationPeriod;
import com.example.backoffice.domain.vacation.entity.VacationPeriodProvider;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JsonCustomException;
import com.example.backoffice.global.redis.UpdateVacationPeriodRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Slf4j
@Component
@RequiredArgsConstructor
public class VacationPeriodInitializer {

    @Value("${cookie.secure}")
    private boolean isSecure;

    private final VacationPeriodProvider vacationPeriodProvider;
    private final UpdateVacationPeriodRepository vacationPeriodRepository;

    // 최초의 한 번만 시행
    // 서버 시작 시 한 번만 실행되는 메서드
    @PostConstruct
    public void initializeVacationPeriodIfNeeded() {
        long currentYear = DateTimeUtils.getToday().getYear();
        long currentMonth = DateTimeUtils.getToday().getMonthValue();

        String key = vacationPeriodProvider.createKey(currentYear, currentMonth);

        if (!vacationPeriodRepository.existsByKey(key)) {
            log.info(
                    currentYear + "년 " + currentMonth
                            + "월에 해당하는 휴가 정정 기간 설정이 캐시에서 존재하지 않습니다.");
            if (vacationPeriodProvider.getVacationPeriod() == null || isPeriodEmpty()) {
                log.info("VacationPeriodProvider에 데이터가 존재하지 않습니다.");
                initializeUpdateVacationPeriod(key, currentYear, currentMonth);
            }else{
                log.info("메모리에 휴가 정정 기간 저장");
                // 현재 날짜 기준으로 해당 달의 첫 번째 날을 가져옴
                LocalDateTime firstDayOfMonth = DateTimeUtils.getFirstDayOfMonth();
                LocalDateTime firstMonday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

                // 두 번째 주의 월요일을 찾음
                LocalDateTime secondMondayOfMonth
                        = DateTimeUtils.findSecondMondayOfMonth(firstMonday);

                LocalDateTime thirdFridayOfMonth = secondMondayOfMonth.plusDays(12).minusSeconds(1);

                initializeMemoryVacationPeriod(secondMondayOfMonth, thirdFridayOfMonth);
            }
        }else{
            log.info("메모리에 휴가 정정 기간 저장");
            String value = vacationPeriodRepository.getValueByKey(key, String.class);

            LocalDateTime upcomingUpdateVacationPeriodStartDay =
                    vacationPeriodProvider.calculateUpcomingStartDate(key, value);
            LocalDateTime upcomingUpdateVacationPeriodEndDay =
                    vacationPeriodProvider.calculateUpcomingEndDate(key, value);
            initializeMemoryVacationPeriod(
                    upcomingUpdateVacationPeriodStartDay,
                    upcomingUpdateVacationPeriodEndDay);
        }

        log.info("초기화된 휴가 신청 시작일 : " + vacationPeriodProvider.getVacationPeriod().getStartDate());
        log.info("초기화된 휴가 신청 마감일 : " + vacationPeriodProvider.getVacationPeriod().getEndDate());
    }

    // VacationPeriodProvider의 기간이 설정되지 않은지 확인하는 메서드
    private boolean isPeriodEmpty() {
        VacationPeriod period = vacationPeriodProvider.getVacationPeriod();
        return period.getStartDate() == null || period.getEndDate() == null;
    }

    private void initializeMemoryVacationPeriod(
            LocalDateTime upcomingUpdateVacationPeriodStartDay,
            LocalDateTime upcomingUpdateVacationPeriodEndDay) {
        if(!isSecure) {
            // application-local.yml
            LocalDateTime today = DateTimeUtils.getToday();
            vacationPeriodProvider.setVacationPeriod(
                    VacationPeriod.builder()
                            .startDate(today)
                            .endDate(DateTimeUtils.getEndDayOfMonth(
                                    (long) today.getYear(),
                                    (long) today.getMonthValue()))
                            .build()
            );
        }else{
            // application-prod.yml
            vacationPeriodProvider.setVacationPeriod(
                    VacationPeriod.builder()
                            .startDate(upcomingUpdateVacationPeriodStartDay)
                            .endDate(upcomingUpdateVacationPeriodEndDay)
                            .build()
            );
        }
    }

    private void initializeUpdateVacationPeriod(
            String key, Long currentYear, Long currentMonth){
        // 현재 날짜 기준으로 해당 달의 첫 번째 날을 가져옴
        LocalDateTime firstDayOfMonth = DateTimeUtils.getFirstDayOfMonth();
        LocalDateTime firstMonday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        // 두 번째 주의 월요일을 찾음
        LocalDateTime secondMondayOfMonth
                = DateTimeUtils.findSecondMondayOfMonth(firstMonday);

        LocalDateTime thirdFridayOfMonth = secondMondayOfMonth.plusDays(12).minusSeconds(1);

        long ttlMinutes = DateTimeUtils.calculateMinutesFromTodayToEndDate(
                DateTimeUtils.getEndDayOfMonth(currentYear, currentMonth));

        String values = vacationPeriodProvider.createValues(
                secondMondayOfMonth.getDayOfMonth(), thirdFridayOfMonth.getDayOfMonth());
        try{
            vacationPeriodRepository.saveMonthlyVacationPeriod(
                    key, Math.toIntExact(ttlMinutes), values);
        }catch (JsonProcessingException e) {
            throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
        }

        initializeMemoryVacationPeriod(secondMondayOfMonth, thirdFridayOfMonth);
    }
}
