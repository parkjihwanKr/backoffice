package com.example.backoffice.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDateRangeDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
