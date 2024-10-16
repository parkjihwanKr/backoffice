package com.example.backoffice.global.initializer;

import com.example.backoffice.domain.vacation.entity.VacationPeriod;
import com.example.backoffice.domain.vacation.entity.VacationPeriodHolder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Slf4j
@Component
@RequiredArgsConstructor
public class VacationPeriodInitializer {

    private final VacationPeriodHolder vacationPeriodHolder;

    // 최초의 한 번만 시행
    // 서버 시작 시 한 번만 실행되는 메서드
    @PostConstruct
    public void initializeVacationPeriodIfNeeded() {
        // VacationPeriodHolder가 비어 있는지 확인
        if (vacationPeriodHolder.getVacationPeriod() == null || isPeriodEmpty()) {
            log.info("VacationPeriodHolder가 비어 있습니다. 기본 값을 초기화합니다.");
            initializeVacationPeriod();
        }
    }

    // VacationPeriodHolder의 기간이 설정되지 않은지 확인하는 메서드
    private boolean isPeriodEmpty() {
        VacationPeriod period = vacationPeriodHolder.getVacationPeriod();
        return period.getStartDate() == null || period.getEndDate() == null;
    }

    private void initializeVacationPeriod() {
        // 현재 날짜 기준으로 해당 달의 첫 번째 날을 가져옴
        LocalDateTime firstDayOfMonth
                = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        LocalDateTime firstMonday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        // 두 번째 주의 월요일을 찾음
        LocalDateTime secondMonday;
        if (firstMonday.getDayOfMonth() <= 7) {
            // 첫 번째 월요일이 1일에서 7일 사이에 있을 경우, 그 주를 두 번째 주로 간주
            secondMonday = firstMonday;
        } else {
            // 첫 번째 월요일이 7일 이후일 경우, 그 다음 주 월요일이 두 번째 주의 월요일
            secondMonday = firstMonday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        // 두 번째 주의 월요일부터 세 번째 주의 금요일까지 설정
        // 기간은 월요일부터 토요일이지만
        LocalDateTime startDate = secondMonday;
        LocalDateTime endDate = startDate.plusDays(12).minusSeconds(1);

        // VacationPeriodHolder에 설정된 값을 저장
        vacationPeriodHolder.setVacationPeriod(
                VacationPeriod.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build()
        );

        log.info("초기화된 휴가 신청 시작일 : " + vacationPeriodHolder.getVacationPeriod().getStartDate());
        log.info("초기화된 휴가 신청 마감일 : " + vacationPeriodHolder.getVacationPeriod().getEndDate());
    }
}
