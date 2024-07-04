package com.example.backoffice.domain.question.controller;

import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.service.QuestionsServiceV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class QuestionsController {

    private final QuestionsServiceV1 questionsService;

    @PostMapping("/evaluations-department/{evaluationId}/questions")
    public ResponseEntity<QuestionsResponseDto.CreateAllDto> createAllForDepartment(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.CreateAllDto requestDto){
        QuestionsResponseDto.CreateAllDto responseDto =
                questionsService.createAllForDepartment(evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations-department/{evaluationId}/questions/{questionId}")
    public ResponseEntity<QuestionsResponseDto.UpdateOneDto> updateOneForDepartment(
            @PathVariable Long evaluationId, @PathVariable Long questionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.UpdateOneDto requestDto){
        QuestionsResponseDto.UpdateOneDto responseDto
                = questionsService.updateOneForDepartment(
                        evaluationId, questionId, memberDetails.getMembers(), requestDto);
        return null;
    }

    @PatchMapping("/evaluations-department/{evaluationId}/questions/{questionId}/change-order")
    public ResponseEntity<QuestionsResponseDto.UpdateOneForOrderDto> updateOneForChangedOrder(
            @PathVariable Long evaluationId, @PathVariable Long questionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.UpdateOneForOrderDto requestDto){
        QuestionsResponseDto.UpdateOneForOrderDto responseDto
                =  questionsService.updateOneForChangedOrder(
                        evaluationId, questionId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 1개에서 ~ 여럿
    @DeleteMapping("/evaluations-department/{evaluationId}/questions")
    public ResponseEntity<CommonResponse<Void>> delete(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.DeleteDto requestDto){
        questionsService.delete(
                evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "성공적으로 삭제되었습니다.", null
                )
        );
    }
}
