package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.facade.EventsServiceFacadeV1;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.memberEvaluation.service.MembersEvaluationsServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final MembersServiceFacadeV1 membersServiceFacade;
    private final EventsServiceFacadeV1 eventsServiceFacade;
    private final EvaluationsServiceV1 evaluationsService;
    private final MembersEvaluationsServiceV1 membersEvaluationsService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;

    // 매일 오전 00시마다 member 휴가 상태 체크
    @Transactional
    // 초 분 시 일 월 요일
    @Scheduled(cron = "0 0 0 * * *")
    public void updateMemberOnVacation() {
        long year = LocalDateTime.now().getYear();
        long month = LocalDateTime.now().getMonthValue();
        long day = LocalDateTime.now().getDayOfMonth();

        // 휴가가 끝난 멤버들의 상태를 false로 설정
        List<Events> endedVacationList
                = eventsServiceFacade.findAllByEventTypeAndEndDateBefore(year, month, day);
        for (Events event : endedVacationList) {
            membersServiceFacade.updateOneForOnVacationFalse(event.getMember().getMemberName());
        }

        // 휴가가 시작된 멤버들의 상태를 true로 설정
        List<Events> startedVacationList
                = eventsServiceFacade.findAllByEventTypeAndStartDateBetween(year, month, day);
        for (Events event : startedVacationList) {
            membersServiceFacade.updateOneForOnVacationTrue(event.getMember().getMemberName());
        }
    }

    // 매달 1일 자정 10분
    @Transactional
    @Scheduled(cron = "0 10 0 1 * *")
    public void updateRemainingVacationDaysMonthly() {
        membersServiceFacade.updateOneForRemainingVacationDays(ScheduledEventType.MONTHLY_UPDATE);
    }

    // 매년 1월 1일 00시 10분
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
}
