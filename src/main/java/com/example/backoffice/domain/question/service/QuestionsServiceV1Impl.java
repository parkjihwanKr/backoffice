package com.example.backoffice.domain.question.service;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.question.converter.QuestionsConverter;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;
import com.example.backoffice.domain.question.entity.QuestionsType;
import com.example.backoffice.domain.question.exception.QuestionsCustomException;
import com.example.backoffice.domain.question.exception.QuestionsExceptionCode;
import com.example.backoffice.domain.question.repository.QuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionsServiceV1Impl implements QuestionsServiceV1{

    private final MembersServiceV1 membersService;
    private final EvaluationsServiceV1 evaluationsService;
    private final QuestionsRepository questionsRepository;

    @Override
    @Transactional
    public QuestionsResponseDto.CreateAllDto createAllForDepartment(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.CreateAllDto requestDto){
        // 1. 해당 평가가 존재하는지? -> 평가가 존재해야 질문을 만듦
        Evaluations evaluation = evaluationsService.findById(evaluationId);

        // 2. 해당 질문을 만들 권한이 있는지?
        // 2-1. 평가를 만든 폼은 이미 검증을 끝낸 상태임. 이를 또 검증할 필요는 없음.
        // 2-2. 평가를 만든 페이지에서 조회를 하는 것이 아닌 바로 url을 다이렉트로 들어오면 막을 방법이 있는지?
        MemberDepartment department = evaluation.getDepartment();

        if((department.equals(loginMember.getDepartment())
                && loginMember.getPosition().equals(MemberPosition.MANAGER))
                || loginMember.getPosition().equals(MemberPosition.CEO)){
            throw new QuestionsCustomException(
                    QuestionsExceptionCode.NOT_PERMISSION_DEPARTMENT_OR_POSITION);
        }

        // 3. requestDto.getQuestionType()이 signleAnswer로 체크 되어 있는데 객관식 문제를 만들 수 있으면 안됨
        // 프론트에서 드롭다운 형태로 값을 받아올 것이기에 이상한 형태의 값을 받는 것만 방지
        // 4. Question Entity 생성
        // 5. 부모인 Evaluations에 추가
        List<QuestionsResponseDto.CreateOneDto> responseDtoList = new ArrayList<>();
        Integer questionsNumber = 1;
        for(QuestionsRequestDto.CreateOneDto requestQuestion : requestDto.getQuestionList()){
            QuestionsType questionsType
                    = QuestionsConverter.toQuestionsType(requestQuestion.getQuestionType());
            for(String multipleChoiceAnswer : requestQuestion.getMultipleChoiceAnswerList()){
                // 3-1. 객관식 질문에 대한 답을 적지 않았을 때
                if(multipleChoiceAnswer.isEmpty()
                        && questionsType.equals(QuestionsType.MULTIPLE_CHOICE_ANSWER)){
                    throw new QuestionsCustomException(QuestionsExceptionCode.NOT_EMPTY_ANSWER);
                }
                // 3-2. 주관식 질문에 대한 객관식 답을 적었을 때
                // 객관식이 체크되면 주관식 질문을 적을 수 없게 할 것이지만 잘못된 요청이 있을 수 있기에
                if(!multipleChoiceAnswer.isEmpty()
                        && questionsType.equals(QuestionsType.SHORT_ANSWER)){
                    throw new QuestionsCustomException(QuestionsExceptionCode.NOT_NEED_ANSWER);
                }
            }

            Questions question
                    = QuestionsConverter.toEntity(
                            evaluation, questionsType, requestQuestion.getQuestionText());

            questionsRepository.save(question);
            evaluation.addQuestion(question);
            responseDtoList.add(
                    QuestionsConverter.toCreateOneDto(
                            question.getQuestionText(), question.getQuestionsType(),
                            questionsNumber, requestQuestion.getMultipleChoiceAnswerList()));
            questionsNumber++;
        }

        return QuestionsConverter.toCreateAllDto(
                evaluation.getTitle(), evaluation.getYear(),
                evaluation.getQuarter(), responseDtoList);
    }
}
