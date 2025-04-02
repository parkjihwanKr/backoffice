package com.example.backoffice.global.redis.service;

import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.redis.repository.UpcomingAttendancesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UpcomingAttendancesServiceV1 {

    private final UpcomingAttendancesRepository upcomingAttendancesRepository;

    public <T> T getValue(Long memberId, Class<T> valueType){
        return upcomingAttendancesRepository.getValue(memberId, valueType);
    }

    public Map<String, String> getAllRawValues() {
        return upcomingAttendancesRepository.getAllRawValues();
    }

    public Long extractMemberIdFromKey(String key) {
        return upcomingAttendancesRepository.extractMemberIdFromKey(key);
    }

    public String extractDescriptionFromKey(String key) {
        return upcomingAttendancesRepository.extractDescriptionFromKey(key);
    }

    public <T> T deserializeValue(String value, Class<T> valueType) {
        return upcomingAttendancesRepository.deserializeValue(value, valueType);
    }

    public <T> void save(Long memberId, DateRange dateRange, String description){
        upcomingAttendancesRepository.saveOne(memberId, dateRange, description);
    }
}
