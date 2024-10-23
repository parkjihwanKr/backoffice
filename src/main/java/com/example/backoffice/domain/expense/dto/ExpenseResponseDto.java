package com.example.backoffice.domain.expense.dto;

import com.example.backoffice.domain.expense.entity.ExpenseProcess;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ExpenseResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto{
        private Long expenseId;
        private String title;
        private String details;
        private String memberName;
        private MemberDepartment department;
        private BigDecimal money;
        private List<FilesResponseDto.ReadOneDto> fileList;
        private ExpenseProcess expenseProcess;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneForProcessDto{
        private Long expenseId;
        private String title;
        private String memberName;
        private MemberDepartment department;
        private ExpenseProcess expenseProcess;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneDto {
        private Long expenseId;
        private String title;
        private String memberName;
        private String details;
        private BigDecimal money;
        private ExpenseProcess process;
        private MemberDepartment department;
        private List<FilesResponseDto.ReadOneDto> fileList;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneDto {
        private Long expenseId;
        private String title;
        private String memberName;
        private String details;
        private BigDecimal money;
        private ExpenseProcess process;
        private MemberDepartment department;
        private List<FilesResponseDto.ReadOneDto> fileList;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
