package com.example.backoffice.domain.expense.service;

import com.example.backoffice.domain.expense.dto.ExpenseRequestDto;
import com.example.backoffice.domain.expense.dto.ExpenseResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpenseServiceV1 {

    ExpenseResponseDto.CreateOneDto createOne(
            String department, Members loginMember,
            ExpenseRequestDto.CreateOneDto requestDto,
            List<MultipartFile> fileList);

    ExpenseResponseDto.UpdateOneForProcessDto updateOneForProcess(
            Long expenseId, String department,
            String process, Members loginMember);

    ExpenseResponseDto.ReadOneDto readOne(
            Long expenseId, Members loginMember);

    Page<ExpenseResponseDto.ReadOneDto> readFiltered(
            ExpenseRequestDto.ReadFilteredDto requestDto,
            Members loginMember, Pageable pageable);

    ExpenseResponseDto.UpdateOneDto updateOne(
            Long expenseId, List<MultipartFile> fileList,
            Members loginMember, ExpenseRequestDto.UpdateOneDto requestDto);

    void deleteOne(Long expenseId, Members loginMember);
}
