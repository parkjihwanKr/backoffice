package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.service.EventsService;
import com.example.backoffice.domain.member.fascade.MembersServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final MembersServiceFacade membersServiceFacade;
    private final EventsService eventsService;

    // 매일 오전 00시마다 member 휴가 상태 체크
    @Transactional
    // 초 분 시 일 월 요일
    @Scheduled(cron = "0 0 0 * * *")
    public void updateMemberOnVacation(){
        long year = (long) LocalDateTime.now().getYear();
        long month = (long) LocalDateTime.now().getMonthValue();
        long day = (long) LocalDateTime.now().getDayOfMonth();

        // 휴가가 끝난 멤버들의 상태를 false로 설정
        List<Events> endedVacationList
                = eventsService.findAllByEventTypeAndEndDateBefore(year, month, day);
        for (Events event : endedVacationList) {
            membersServiceFacade.updateOnVacationFalse(event.getMember().getMemberName());
        }

        // 휴가가 시작된 멤버들의 상태를 true로 설정
        List<Events> startedVacationList
                = eventsService.findAllByEventTypeAndStartDateBetween(year, month, day);
        for (Events event : startedVacationList) {
            membersServiceFacade.updateOnVacationTrue(event.getMember().getMemberName());
        }
    }

    // 매달 1일 자정 10분
    @Transactional
    @Scheduled(cron = "0 10 0 1 * *")
    public void updateRemainingVacationDaysMonthly() {
        membersServiceFacade.updateRemainingVacationDays(ScheduledEventType.MONTHLY_UPDATE);
    }

    // 매년 1월 1일 00시 10분
    @Transactional
    @Scheduled(cron = "0 20 0 1 1 *")
    public void updateRemainingVacationDaysYearly() {
        membersServiceFacade.updateRemainingVacationDays(ScheduledEventType.YEARLY_UPDATE);
    }
}
