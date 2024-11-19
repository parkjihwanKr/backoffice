package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class YearlyScheduler implements SchedulerTask{

    private final MembersServiceV1 membersService;
    private final AttendancesServiceV1 attendancesService;

    @Override
    public void execute(){
        updateRemainingVacationDays();
    }

    private void updateRemainingVacationDays() {
        membersService.updateOneForRemainingVacationDays(ScheduledEventType.YEARLY_UPDATE);
    }

    private void deleteAttendanceList(){
         List<Long> memberIdList
                 = membersService.findAll().stream().map(
                         Members::getId).toList();
         attendancesService.delete(memberIdList);
    }
}
