package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.memberEvaluation.service.MembersEvaluationsServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import com.example.backoffice.global.date.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyScheduler implements SchedulerTask{

    private final MembersServiceV1 membersService;
    private final EvaluationsServiceV1 evaluationsService;
    private final MembersEvaluationsServiceV1 membersEvaluationsService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;
    private final VacationsServiceV1 vacationsService;
    private final AttendancesServiceV1 attendancesService;

    @Override
    public void execute() {
        refreshCachedDateData();
        sendNotificationForUnCompletedEvaluationMember();
        updateMemberOnVacation();
        createAttendances();
        updateYesterdayAttendanceStatus();
    }

    private void refreshCachedDateData(){
        DateTimeUtils.refreshCached();
    }

    private void sendNotificationForUnCompletedEvaluationMember(){
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
                                unCompletedEvaluationMember, membersService.findHRManager(),
                                null, null, null, null, message),
                        NotificationType.EVALUATION
                );
            }
        }
    }

    private void updateMemberOnVacation() {
        // 휴가가 끝난 멤버들의 상태를 false로 설정
        List<Vacations> endedVacationList
                = vacationsService.findAllBetweenYesterday(
                        DateTimeUtils.getToday().minusSeconds(1));
        for (Vacations vacation : endedVacationList) {
            membersService.updateOneForOnVacationFalse(vacation.getOnVacationMember().getId());
        }

        // 휴가가 시작된 멤버들의 상태를 true로 설정
        List<Vacations> startedVacationList
                = vacationsService.findAllByStartDate(DateTimeUtils.getToday());
        for (Vacations vacation : startedVacationList) {
            membersService.updateOneForOnVacationTrue(vacation.getOnVacationMember().getId());
        }
    }

    // 여기가 에러가 나는듯
    private void createAttendances(){
        // 평일
        if(DateTimeUtils.isWeekday()){
            attendancesService.create(true);
        }else{
            attendancesService.create(false);
        }
    }

    private void updateYesterdayAttendanceStatus(){
        attendancesService.updateYesterdayAttendanceList();
    }
}