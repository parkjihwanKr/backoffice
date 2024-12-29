package com.example.backoffice.domain.finance.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.openBank.token.OpenBankResponseToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class FinanceResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneDto {
        private Long financeId;
        private Long bankId;
        private String description;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDepartmentBudgetDto {
        private Long financeId;
        private Long bankId;
        private String description;
        private MemberDepartment department;
        private BigDecimal allocatedDepartmentBudget;
        private int expires_in;
    }
}
