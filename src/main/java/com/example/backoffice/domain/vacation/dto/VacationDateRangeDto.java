package com.example.backoffice.domain.vacation.dto;

import com.example.backoffice.global.common.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacationDateRangeDto {
    private DateRange dateRange;
}
