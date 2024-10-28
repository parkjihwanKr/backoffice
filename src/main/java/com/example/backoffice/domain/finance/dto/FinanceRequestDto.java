package com.example.backoffice.domain.finance.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class FinanceRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDepartmentBudgetDto {
        private String description;
        private MemberDepartment department;
        private BigDecimal allocatedBudget;
    }
}
