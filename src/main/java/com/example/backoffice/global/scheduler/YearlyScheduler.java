package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.member.service.MembersServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class YearlyScheduler implements SchedulerTask{

    private final MembersServiceV1 membersService;
    @Override
    public void execute(){
        updateRemainingVacationDays();
    }

    private void updateRemainingVacationDays() {
        membersService.updateOneForRemainingVacationDays(ScheduledEventType.YEARLY_UPDATE);
    }

}
