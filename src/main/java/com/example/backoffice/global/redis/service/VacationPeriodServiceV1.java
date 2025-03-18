package com.example.backoffice.global.redis.service;

import com.example.backoffice.domain.vacation.entity.VacationPeriodProvider;

public interface VacationPeriodServiceV1 {
    /**
     * 해당하는 휴가 정정 기간이 존재하는지
     * @param key : redis key
     */
    Boolean existPeriod(String key);

    /**
     * 해당하는 휴가 정정 기간을 삭제
     * @param key : redis key
     */
    void deletePeriodByKey(String key);

    /**
     * 휴가 정정 기간 저장
     * @param key : redis key
     * @param minutes : ttl
     * @param values : redis values
     * @param <T> : String type
     */
    <T> void save(String key, int minutes, T values);

    /**
     * 휴가 정정 기간의 key을 통한 시작일과 마지막 일 찾기
     * @param key {@link VacationPeriodProvider}
     * 해당 링크를 통해 만들어진 키
     * @return 해당하는 키에 대한 value
     */
    <T> T getValueByKey(String key, Class<T> valueType);
}
