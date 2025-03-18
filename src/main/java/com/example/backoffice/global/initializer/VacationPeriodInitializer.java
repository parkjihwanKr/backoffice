package com.example.backoffice.global.initializer;

import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.entity.VacationPeriod;
import com.example.backoffice.domain.vacation.entity.VacationPeriodProvider;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.redis.service.VacationPeriodServiceImplV1;
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
    private final VacationPeriodServiceImplV1 vacationPeriodCacheService;

    // 최초의 한 번만 시행
    // 서버 시작 시 한 번만 실행되는 메서드
    @PostConstruct
    public void initializeVacationPeriodIfNeeded() {
        long currentYear = DateTimeUtils.getToday().getYear();
        long currentMonth = DateTimeUtils.getToday().getMonthValue();

        String key = vacationPeriodProvider.createKey(currentYear, currentMonth);

        if (!vacationPeriodCacheService.existsByKey(key)) {
            log.info("Redis에 존재하지 않음, 휴가 정정 기간 저장 프로세스 시작");
            initializeUpdateVacationPeriod(key, currentYear, currentMonth);

            log.info("초기화된 휴가 신청 시작일 : " );
            log.info("초기화된 휴가 신청 마감일 : " );
        }
    }

    // VacationPeriodProvider의 기간이 설정되지 않은지 확인하는 메서드
    private boolean isPeriodEmpty() {
        VacationPeriod period = vacationPeriodProvider.getVacationPeriod();
        return period.getStartDate() == null || period.getEndDate() == null;
    }

    private void initializeUpdateVacationPeriod(
            String key, Long currentYear, Long currentMonth){
        if(isSecure){
            log.info("배포 환경 .. ");
            // 현재 날짜 기준으로 해당 달의 첫 번째 날을 가져옴
            LocalDateTime firstDayOfMonth = DateTimeUtils.getFirstDayOfMonth();
            LocalDateTime firstMonday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

            // 두 번째 주의 월요일을 찾음
            LocalDateTime secondMondayOfMonth
                    = DateTimeUtils.findSecondMondayOfMonth(firstMonday);

            LocalDateTime thirdFridayOfMonth = secondMondayOfMonth.plusDays(12).minusSeconds(1);

            long ttlMinutes = DateTimeUtils.calculateMinutesFromTodayToEndDate(
                    DateTimeUtils.getEndDayOfMonth(currentYear, currentMonth));

            VacationsResponseDto.ReadPeriodDto values
                    = vacationPeriodProvider.createValues(
                            secondMondayOfMonth, thirdFridayOfMonth);
                vacationPeriodCacheService.save(
                        key, Math.toIntExact(ttlMinutes), values);
        }else{
            log.info("개발 환경 .. ");
            LocalDateTime endDayOfMonth
                    = DateTimeUtils.getEndDayOfMonth(currentYear, currentMonth);
            VacationsResponseDto.ReadPeriodDto values
                    = vacationPeriodProvider.createValues(
                            DateTimeUtils.getCurrentDateTime(), endDayOfMonth);
            long ttlMinutes = DateTimeUtils.calculateMinutesFromTodayToEndDate(endDayOfMonth);
                vacationPeriodCacheService.save(
                        key, Math.toIntExact(ttlMinutes), values);
        }
    }
}
