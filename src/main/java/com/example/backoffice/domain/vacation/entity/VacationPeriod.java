package com.example.backoffice.domain.vacation.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VacationPeriod {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isWithinAllowedPeriod(LocalDateTime today) {
        LocalDateTime startDate = this.startDate;
        LocalDateTime endDate = this.endDate;

        return (today.isEqual(startDate) || today.isAfter(startDate)) &&
                (today.isEqual(endDate) || today.isBefore(endDate));
    }

    public boolean isEmpty(){
        if(startDate == null && endDate == null){
            return true;
        }
        return false;
    }
}
