package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.vacation.entity.VacationPeriodHolder;
import com.example.backoffice.global.date.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "monthlyScheduler")
public class MonthlyScheduler implements SchedulerTask{

    private final MembersServiceV1 membersService;
    private final VacationPeriodHolder vacationPeriodHolder;

    @Override
    public void execute(){
        updateRemainingVacationDays();
        configureVacationRequestPeriod();
    }

    private void updateRemainingVacationDays() {
        membersService.updateOneForRemainingVacationDays(
                ScheduledEventType.MONTHLY_UPDATE);
    }


    private void configureVacationRequestPeriod() {
        // 현재 날짜 기준으로 해당 달의 첫 번째 날을 가져옴
        LocalDateTime firstDayOfMonth = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime firstMonday = firstDayOfMonth.plusDays(
                (8 - firstDayOfMonth.getDayOfWeek().getValue()) % 7);

        // 두 번째 주의 월요일을 찾음
        LocalDateTime secondMonday;
        if (firstMonday.getDayOfMonth() > 7) {
            // 첫 번째 월요일이 8일 이후일 경우, 첫 번째 월요일이 두 번째 주의 월요일임
            secondMonday = firstMonday;
        } else {
            // 첫 번째 월요일이 7일 이전일 경우, 다음 주 월요일이 두 번째 주의 월요일
            secondMonday = firstMonday.plusWeeks(1);
        }

        // 두 번째 주의 월요일부터
        // 세 번째 주의 금요일까지
        LocalDateTime startDate = secondMonday;
        LocalDateTime endDate = startDate.plusDays(11); // 월요일 + 4일 -> 금요일

        // localhost test용
        /*LocalDateTime today = DateTimeUtils.getToday();
        vacationPeriodHolder.setVacationPeriod(
                DateTimeUtils.getToday(),
                DateTimeUtils.getEndDayOfMonth(
                        (long) today.getYear(), (long) today.getMonthValue())
        );

        log.info("로컬 환경 휴가 신청 기간 설정 : "
                +vacationPeriodHolder.getVacationPeriod().getStartDate() + " ~ "
        +vacationPeriodHolder.getVacationPeriod().getEndDate());*/

        // 휴가 신청 기간을 VacationPeriod에 저장
        vacationPeriodHolder.setVacationPeriod(startDate, endDate);
    }
}
