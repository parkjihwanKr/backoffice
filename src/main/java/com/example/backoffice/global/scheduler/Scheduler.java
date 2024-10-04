package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
import com.example.backoffice.domain.event.facade.EventsServiceFacadeV1;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.memberEvaluation.service.MembersEvaluationsServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import com.example.backoffice.domain.vacation.entity.VacationPeriod;
import com.example.backoffice.domain.vacation.entity.VacationPeriodHolder;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final MembersServiceFacadeV1 membersServiceFacade;
    private final EventsServiceFacadeV1 eventsServiceFacade;
    private final EvaluationsServiceV1 evaluationsService;
    private final VacationsServiceV1 vacationsService;
    private final MembersEvaluationsServiceV1 membersEvaluationsService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;
    private final VacationPeriodHolder vacationPeriodHolder;

    // 매달 1일 자정 10분
    @Transactional
    @Scheduled(cron = "0 10 0 1 * *")
    public void updateRemainingVacationDaysMonthly() {
        membersServiceFacade.updateOneForRemainingVacationDays(ScheduledEventType.MONTHLY_UPDATE);
    }

    // 매년 1월 1일 00시 20분
    @Transactional
    // 초 분 시 일 월 요일
    @Scheduled(cron = "0 20 0 1 1 *")
    public void updateRemainingVacationDaysYearly() {
        membersServiceFacade.updateOneForRemainingVacationDays(ScheduledEventType.YEARLY_UPDATE);
    }

    // 매일 자정 30분에 검사
    @Transactional
    @Scheduled(cron = "0 30 0 * * *")
    public void sendNotificationForUnCompletedEvaluationMember(){
        List<Evaluations> evaluationList
                = evaluationsService.findAllByEndDatePlusSevenDays(LocalDate.now().plusDays(7));
        if(!evaluationList.isEmpty()){
            List<Members> unCompletedEvaluationMemberList
                    = membersEvaluationsService.findAllByIsCompleted(false).stream()
                    .map(MembersEvaluations::getMember).toList();
            for(Members unCompletedEvaluationMember : unCompletedEvaluationMemberList){
                String message = "설문 조사 마감 7일 전입니다. 신속히 마무리 해주시길 바랍니다.";
                notificationsServiceFacade.createOne(
                        NotificationsConverter.toNotificationData(
                                unCompletedEvaluationMember, membersServiceFacade.findHRManager(),
                                null, null, null, null, message),
                        NotificationType.EVALUATION
                );
            }
        }
    }

    // 휴가 신청 기간을 매달 자정 40분에 매달 두번째 월요일부터 금요일까지 자동 설정
    @Scheduled(cron = "0 40 0 1 * ?")
    public void configureVacationRequestPeriod() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime secondMonday = getSecondMondayOfMonth(now.getYear(), now.getMonthValue());
        LocalDateTime secondFriday = secondMonday.plusDays(4);

        // 해당 월의 두 번째 월요일부터 금요일까지를 설정
        vacationPeriodHolder.setVacationPeriod(secondMonday, secondFriday);
    }

    // 매일 오전 00시마다 member 휴가 상태 체크
    @Transactional
    // 초 분 시 일 월 요일
    @Scheduled(cron = "0 0 0 * * *")
    public void updateMemberOnVacation() {
        LocalDateTime now = LocalDateTime.now();

        // 휴가가 끝난 멤버들의 상태를 false로 설정
        List<Vacations> endedVacationList
                = vacationsService.findAllByEndDateBefore(now);
        for (Vacations vacation : endedVacationList) {
            membersServiceFacade.updateOneForOnVacationFalse(vacation.getOnVacationMember().getId());
        }

        // 휴가가 시작된 멤버들의 상태를 true로 설정
        List<Vacations> startedVacationList
                = vacationsService.findAllByStartDate(now);
        for (Vacations vacation : startedVacationList) {
            membersServiceFacade.updateOneForOnVacationTrue(vacation.getOnVacationMember().getId());
        }
    }

    private LocalDateTime getSecondMondayOfMonth(int year, int month) {
        return LocalDateTime.of(year, month, 1, 0, 0)
                .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY));
    }
}
