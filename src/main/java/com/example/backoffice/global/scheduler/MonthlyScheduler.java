package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.vacation.entity.VacationPeriodProvider;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JsonCustomException;
import com.example.backoffice.global.redis.UpdateVacationPeriodRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "monthlyScheduler")
public class MonthlyScheduler implements SchedulerTask{

    private final MembersServiceV1 membersService;
    private final AttendancesServiceV1 attendancesService;
    private final VacationPeriodProvider vacationPeriodProvider;
    private final UpdateVacationPeriodRepository vacationPeriodRepository;

    @Override
    public void execute(){
        updateRemainingVacationDays();
        configureVacationRequestPeriod();
        deleteBeforeTwoYearAttendanceList();
    }

    private void updateRemainingVacationDays() {
        membersService.updateOneForRemainingVacationDays(
                ScheduledEventType.MONTHLY_UPDATE);
    }

    private void configureVacationRequestPeriod() {
        // 현재 날짜 기준으로 해당 달의 첫 번째 날을 가져옴
        LocalDateTime firstDayOfMonth = DateTimeUtils.getFirstDayOfMonth();
        LocalDateTime firstMonday
                = DateTimeUtils.getFirstMonday(firstDayOfMonth);

        Long currentYear = (long) DateTimeUtils.getToday().getYear();
        Long currentMonth = (long) DateTimeUtils.getToday().getMonthValue();

        // 두 번째 주의 월요일을 찾음
        LocalDateTime secondMondayOfMonth
                = DateTimeUtils.findSecondMondayOfMonth(firstMonday);

        LocalDateTime thirdFridayofMonth = secondMondayOfMonth.plusDays(11); // 월요일 + 4일 -> 금요일

        LocalDateTime endOfDay
                = DateTimeUtils.getEndDayOfMonth(currentYear, currentMonth);
        Long ttlMinutes
                = DateTimeUtils.calculateMinutesFromTodayToEndDate(endOfDay);
        // 휴가 신청 기간을 VacationPeriod에 저장
        vacationPeriodProvider.setVacationPeriod(secondMondayOfMonth, thirdFridayofMonth);
        try {
            vacationPeriodRepository.saveMonthlyVacationPeriod(
                    vacationPeriodProvider.createKey(currentYear, currentMonth),
                    Math.toIntExact(ttlMinutes),
                    vacationPeriodProvider.createValues(
                            secondMondayOfMonth.getDayOfMonth(), thirdFridayofMonth.getDayOfMonth()));
        }catch (JsonProcessingException e) {
            throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
        }
    }

    private void deleteBeforeTwoYearAttendanceList(){
        attendancesService.delete();
    }
}
