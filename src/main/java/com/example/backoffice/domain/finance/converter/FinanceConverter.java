package com.example.backoffice.domain.finance.converter;

import com.example.backoffice.domain.expense.entity.Expense;
import com.example.backoffice.domain.finance.dto.FinanceResponseDto;
import com.example.backoffice.domain.finance.entity.Finance;
import com.example.backoffice.domain.openBank.config.OpenBankProperties;
import com.example.backoffice.domain.openBank.dto.TokenRequestDto;
import com.example.backoffice.domain.openBank.token.OpenBankRequestToken;

import java.math.BigDecimal;
import java.util.List;

public class FinanceConverter {

    // 가져와야함
    public static Finance toEntity(
            BigDecimal totalBalance, String currency, Long bankId,
            String description, String responseCode,
            String responseMessage, Integer expiredTime,
            List<Expense> expenseList){
        return Finance.builder()
                .totalBalance(totalBalance)
                .currency(currency)
                .bankId(bankId)
                .expenseList(expenseList)
                .description(description)
                .rsp_message(responseMessage)
                .rsp_code(responseCode)
                .expires_in(expiredTime)
                .build();
    }

    public static OpenBankRequestToken toOpenBankRequestToken(
            TokenRequestDto tokenRequestDto, OpenBankProperties openBankProperties){
        return OpenBankRequestToken.builder()
                .code(tokenRequestDto.getCode())
                .client_id(openBankProperties.getClientId())
                .client_secret(openBankProperties.getClientSecret())
                .redirect_uri(openBankProperties.getRedirectUri())
                .grant_type("authorization_code")
                .build();
    }

    public static TokenRequestDto toTokenRequestDto(){
        return TokenRequestDto.builder()
                .code("")
                .memberId(1L)
                .build();
    }

    public static FinanceResponseDto.CreateDepartmentBudgetDto toCreateDepartmentBudgetDto(
            Finance finance){
        return FinanceResponseDto.CreateDepartmentBudgetDto.builder()
                .allocatedDepartmentBudget(finance.getExpenseList().get(0).getMoney())
                .department(finance.getExpenseList().get(0).getDepartment())
                .expires_in(finance.getExpires_in())
                .description(finance.getDescription())
                .financeId(finance.getId())
                .build();
    }

    public static FinanceResponseDto.ReadOneDto toReadOneDto(Finance finance){
        return FinanceResponseDto.ReadOneDto.builder()
                .financeId(finance.getId())
                .bankId(finance.getBankId())
                .description(finance.getDescription())
                .build();
    }
}
