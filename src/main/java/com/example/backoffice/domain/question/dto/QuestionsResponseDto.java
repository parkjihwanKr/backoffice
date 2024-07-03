package com.example.backoffice.domain.question.dto;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.question.entity.QuestionsType;
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
    public static class CreateOneDto {
        private Integer questionNumber;
        private String questionText;
        // 해당 타입에 따라 multipleChoiceAnswerList 활성화 여부 확인
        // 그렇게 따지면 QuestionsType는 구분 하는게 2개 인데, Boolean으로 여부를 확인하면 좋지 않음?
        // 설문조사 특성에 따라 QuestionsType이 객관식, 주관식로 나뉘긴 하지만 다른 형태로 만들어질 수 있음을 고려
        // 확장성을 위해 Enum으로 만듦 -> 실제로 구현한 것은 QuestionsType은 2개임.
        private QuestionsType questionType;
        // 1. 매우 만족 ~ 5. 매우 불만족
        private List<String> multipleChoiceAnswerList;
    }

    // 해당 부분은 카드의 순서를 바꾸듯 질문의 번호가 바뀌는 형태가 될 수 있음
    // 즉, Question Entity의 고유 아이디가 빨라도 질문의 앞 순서가 되는게 아니라 인덱스 순서대로 밀려나야됨
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneDto{
        private Long questionNumber;
        private String questionText;
        private QuestionsType questionsType;
        private List<Answers> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForOrderDto {
        private Long beforeUpdatedOrder;
        private Long updatedOrder;
        private String questionText;
        private QuestionsType questionsType;
        private List<Answers> multipleChoiceAnswerList;
    }
}
