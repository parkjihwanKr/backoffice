package com.example.backoffice.global.scheduler;

import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class YearlyScheduler implements SchedulerTask{

    private final MembersServiceFacadeV1 membersServiceFacade;

    @Override
    public void execute(){
        updateRemainingVacationDays();
    }

    private void updateRemainingVacationDays() {
        membersServiceFacade.updateOneForRemainingVacationDays(ScheduledEventType.YEARLY_UPDATE);
    }
}
