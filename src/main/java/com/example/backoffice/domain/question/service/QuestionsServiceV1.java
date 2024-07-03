package com.example.backoffice.domain.question.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;

public interface QuestionsServiceV1 {

    QuestionsResponseDto.CreateAllDto createAllForDepartment(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.CreateAllDto requestDto);

    QuestionsResponseDto.UpdateOneDto updateOneForDepartment(
            Long evaluationId, Long questionId,
            Members loginMember, QuestionsRequestDto.UpdateOneDto requestDto);

    QuestionsResponseDto.UpdateOneForOrderDto updateOneForChangedOrder(
            Long evaluationId, Long questionId,
            Members loginMember, QuestionsRequestDto.UpdateOneForOrderDto requestDto);

    Questions findById(Long questionId);
}
