package com.example.backoffice.global.redis.service;

import com.example.backoffice.global.redis.repository.VacationPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VacationPeriodServiceImplV1 implements VacationPeriodServiceV1 {

    private final VacationPeriodRepository vacationPeriodRepository;

    public boolean existsByKey(String key){
        return vacationPeriodRepository.existsByKey(key);
    }

    @Override
    public Boolean existPeriod(String key){
        return vacationPeriodRepository.existsByKey(key);
    }

    @Override
    public void deletePeriodByKey(String key){
        vacationPeriodRepository.delete(key);
    }

    @Override
    public <T> T getValueByKey(String key, Class<T> valueType){
        return vacationPeriodRepository.getValueByKey(key, valueType);
    }

    @Transactional
    public <T> void save(
            String key, int ttlMinutes, T values){
        vacationPeriodRepository.save(key, Math.toIntExact(ttlMinutes), values);
    }
}

