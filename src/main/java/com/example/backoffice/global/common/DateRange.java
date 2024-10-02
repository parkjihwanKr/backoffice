package com.example.backoffice.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRange {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isValid() {
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
}
