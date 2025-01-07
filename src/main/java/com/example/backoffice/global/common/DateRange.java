package com.example.backoffice.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRange {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public List<DateRange> splitUntil(LocalDateTime end) {
        List<DateRange> ranges = new ArrayList<>();
        LocalDateTime currentStart = startDate;

        while (!currentStart.isAfter(end)) {
            LocalDateTime currentEnd = currentStart.toLocalDate().atTime(23, 59, 59);
            if (currentEnd.isAfter(end)) {
                currentEnd = end.minusSeconds(1);
            }
            ranges.add(new DateRange(currentStart, currentEnd));
            currentStart = currentStart.plusDays(1).toLocalDate().atStartOfDay();
        }

        return ranges;
    }

    public List<DateRange> splitFrom(LocalDateTime start) {
        List<DateRange> ranges = new ArrayList<>();
        LocalDateTime currentStart = start;

        while (!currentStart.isAfter(endDate)) {
            LocalDateTime currentEnd = currentStart.toLocalDate().atTime(23, 59, 59);
            if (currentEnd.isAfter(endDate)) {
                currentEnd = endDate;
            }
            ranges.add(new DateRange(currentStart, currentEnd));
            currentStart = currentStart.plusDays(1).toLocalDate().atStartOfDay();
        }

        return ranges;
    }
}
