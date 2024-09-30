package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.repository.VacationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationsServiceImplV1 implements VacationsServiceV1{

    private final VacationsRepository vacationsRepository;

    @Override
    @Transactional
    public VacationsResponseDto.CreateOneDto createOne(
            Members loginMember, VacationsRequestDto.CreateOneDto requestDto){
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationsResponseDto.ReadDayDto> readDay(
            String department, Long year, Long month, Long day, Members loginMember){
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationsResponseDto.ReadMonthForDepartmentDto> readMonthForDepartment(
            String department, Long year, Long month, Members loginMember){
        return null;
    }

    @Override
    @Transactional
    public VacationsResponseDto.UpdateOneDto updateOne(
            Long vacationId, Members loginMember,
            VacationsRequestDto.UpdateOneDto requestDto) {

        return null;
    }

    @Override
    @Transactional
    public void deleteOne(Long vacationId, Members loginMember){

    }
}
