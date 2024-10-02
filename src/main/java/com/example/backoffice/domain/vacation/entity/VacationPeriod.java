package com.example.backoffice.domain.vacation.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Builder
public class VacationPeriod {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isWithinAllowedPeriod(LocalDateTime allowedStartDate, LocalDateTime allowedEndDate) {
        LocalDateTime startDate = this.startDate;
        LocalDateTime endDate = this.endDate;

        return (startDate.isAfter(allowedStartDate) || startDate.isEqual(allowedStartDate)) &&
                (endDate.isBefore(allowedEndDate) || endDate.isEqual(allowedEndDate));
    }
}
