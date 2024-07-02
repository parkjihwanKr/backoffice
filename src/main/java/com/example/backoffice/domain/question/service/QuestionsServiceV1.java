package com.example.backoffice.domain.question.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;

public interface QuestionsServiceV1 {

    QuestionsResponseDto.CreateAllDto createAllForDepartment(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.CreateAllDto requestDto);
}
