package com.example.backoffice.domain.question.dto;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.question.entity.QuestionsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuestionsResponseDto {

    // ForCompany, ForDepartment를 안 쓰는 이유 :
    // 응답 형식이 변경될 거 같지 않음
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsResponseDto.CreateAllDto",
            description = "질문 리스트 생성 응답 DTO")
    public static class CreateAllDto {
        private String evaluationTitle;
        private Integer year;
        private Integer quarter;
        private List<CreateOneDto> questionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsResponseDto.CreateOneDto",
            description = "질문 하나 생성 응답 DTO")
    public static class CreateOneDto {
        private Long questionId;
        private Integer questionNumber;
        private String questionText;
        private QuestionsType questionType;
        private List<String> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsResponseDto.UpdateOneDto",
            description = "질문 하나 수정 응답 DTO")
    public static class UpdateOneDto{
        private Long questionId;
        private Long questionNumber;
        private String questionText;
        private QuestionsType questionsType;
        private List<Answers> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsResponseDto.UpdateOneForOrderDto",
            description = "질문 순서 수정 응답 DTO")
    public static class UpdateOneForOrderDto {
        private Long questionId;
        private Long previousNumber;
        private Long updatedNumber;
        private String questionText;
        private QuestionsType questionsType;
        private List<Answers> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsResponseDto.ReadOneDto",
            description = "질문 하나 조회 응답 DTO")
    public static class ReadOneDto{
        private Long questionId;
        private Long questionNumber;
        private String questionText;
        private QuestionsType questionsType;
        private List<String> multipleAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsResponseDto.SubmitAllDto",
            description = "질문들 제출 응답 DTO")
    public static class SubmitAllDto {
        private String memberName;
        private Boolean checked;
        private List<SubmitOneDto> submitOneDtoList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsResponseDto.SubmitOneDto",
            description = "질문 하나 제출 응답 DTO")
    public static class SubmitOneDto {
        private Long questionId;
        private Long questionNumber;
        private String questionType;
        private String shortAnswer;
        private Long multipleChoiceAnswerNumber;
    }
}
