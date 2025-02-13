package com.example.backoffice.domain.event.dto;

import com.example.backoffice.global.common.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDateRangeDto {
    private DateRange dateRange;
}
