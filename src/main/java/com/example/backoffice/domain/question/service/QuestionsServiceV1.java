package com.example.backoffice.domain.question.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;

public interface QuestionsServiceV1 {

    QuestionsResponseDto.CreateAllDto createAll(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.CreateAllDto requestDto);

    QuestionsResponseDto.UpdateOneDto updateOne(
            Long evaluationId, Long questionId,
            Members loginMember, QuestionsRequestDto.UpdateOneDto requestDto);

    QuestionsResponseDto.UpdateOneForOrderDto updateOneForChangedOrder(
            Long evaluationId, Long questionId,
            Members loginMember, QuestionsRequestDto.UpdateOneForOrderDto requestDto);

    void delete(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.DeleteDto requestDto);

    QuestionsResponseDto.SubmitAllDto submitAll(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.SubmitAllDto requestDto);

    Questions findById(Long questionId);
}
