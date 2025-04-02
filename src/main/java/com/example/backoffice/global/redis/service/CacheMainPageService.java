package com.example.backoffice.global.redis.service;

import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.redis.repository.CacheMainPageRepository;
import com.example.backoffice.global.redis.utils.RedisProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CacheMainPageService {

    private final CacheMainPageRepository cacheMainPageRepository;

    @Transactional
    public <T> void save(
            Long memberId,
            MainPageResponseDto.SummaryExceptBoardDto responseDto){
        cacheMainPageRepository.save(
                RedisProvider.MAIN_PAGE_PREFIX+memberId,
                Math.toIntExact(DateTimeUtils.getAtEndOfDay().toMinutes()),
                responseDto);
    }

    @Transactional
    public void evict(List<Long> departmentMemberIdList){
        cacheMainPageRepository.evict(departmentMemberIdList);
    }
}
