package com.example.backoffice.domain.expense.converter;

import com.example.backoffice.domain.expense.dto.ExpenseResponseDto;
import com.example.backoffice.domain.expense.entity.Expense;
import com.example.backoffice.domain.expense.entity.ExpenseProcess;
import com.example.backoffice.domain.expense.exception.ExpenseCustomException;
import com.example.backoffice.domain.expense.exception.ExpenseExceptionCode;
import com.example.backoffice.domain.file.converter.FilesConverter;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseConverter {

    public static Expense toEntity(
            String title, BigDecimal money, String details, MemberDepartment department){
        return Expense.builder()
                .title(title)
                .money(money)
                .expenseProcess(ExpenseProcess.PENDING)
                .details(details)
                .department(department)
                .build();
    }

    public static ExpenseResponseDto.CreateOneDto toCreateOneDto(
            Expense expense, List<Files> fileList, String memberName){
        List<FilesResponseDto.ReadOneDto> responseFileList
                = fileList.stream().map(
                        file -> FilesResponseDto.ReadOneDto.builder().id(file.getId()).url(file.getUrl()).build()
        ).collect(Collectors.toList());

        return ExpenseResponseDto.CreateOneDto.builder()
                .title(expense.getTitle())
                .expenseId(expense.getId())
                .money(expense.getMoney())
                .department(expense.getDepartment())
                .details(expense.getDetails())
                .memberName(memberName)
                .fileList(responseFileList)
                .build();
    }

    public static ExpenseResponseDto.UpdateOneForProcessDto toUpdateOneForProcessDto(
            Expense expense, String memberName) {
        return ExpenseResponseDto.UpdateOneForProcessDto.builder()
                .title(expense.getTitle())
                .expenseId(expense.getId())
                .department(expense.getDepartment())
                .memberName(memberName)
                .expenseProcess(expense.getExpenseProcess())
                .build();
    }

    public static ExpenseResponseDto.ReadOneDto toReadOneDto(Expense expense) {
        return ExpenseResponseDto.ReadOneDto.builder()
                .title(expense.getTitle())
                .memberName(expense.getMemberName())
                .createdAt(expense.getCreatedAt())
                .modifiedAt(expense.getModifiedAt())
                .money(expense.getMoney())
                .process(expense.getExpenseProcess())
                .department(expense.getDepartment())
                .fileList(expense.getFileList().stream().map(
                        FilesConverter::toReadOneDto).collect(Collectors.toList()))
                .build();
    }

    public static Page<ExpenseResponseDto.ReadOneDto> toReadFilteredPageDto(
            Page<Expense> filteredExpensePage){
        return filteredExpensePage.map(ExpenseConverter::toReadOneDto);
    }

    public static ExpenseResponseDto.UpdateOneDto toUpdateOneDto(Expense expense){
        return ExpenseResponseDto.UpdateOneDto.builder()
                .title(expense.getTitle())
                .money(expense.getMoney())
                .expenseId(expense.getId())
                .process(expense.getExpenseProcess())
                .department(expense.getDepartment())
                .memberName(expense.getMemberName())
                .fileList(expense.getFileList().stream().map(
                        FilesConverter::toReadOneDto).collect(Collectors.toList()))
                .details(expense.getDetails())
                .createdAt(expense.getCreatedAt())
                .modifiedAt(expense.getModifiedAt())
                .build();
    }

    public static ExpenseProcess toProcess(String process){
        return switch(process) {
            case "APPROVED" -> ExpenseProcess.APPROVED;
            case "REJECTED" -> ExpenseProcess.REJECTED;
            default -> throw new ExpenseCustomException(ExpenseExceptionCode.NOT_FOUND_EXPENSE_PROCESS);
        };
    }
}
