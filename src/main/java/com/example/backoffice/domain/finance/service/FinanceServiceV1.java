package com.example.backoffice.domain.finance.service;

import com.example.backoffice.domain.finance.dto.FinanceRequestDto;
import com.example.backoffice.domain.finance.dto.FinanceResponseDto;
import com.example.backoffice.domain.member.entity.Members;

public interface FinanceServiceV1 {

    FinanceResponseDto.ReadOneDto readOne(
            Long financeId, Members loginMember);

    FinanceResponseDto.CreateDepartmentBudgetDto createDepartmentBudget(
            Members loginMember, FinanceRequestDto.CreateDepartmentBudgetDto requestDto);
}
