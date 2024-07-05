package com.example.backoffice.domain.question.service;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.answer.service.AnswersServiceV1;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
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

    private final EvaluationsServiceV1 evaluationsService;
    private final AnswersServiceV1 answersService;
    private final QuestionsRepository questionsRepository;

    @Override
    @Transactional
    public QuestionsResponseDto.CreateAllDto createAll(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.CreateAllDto requestDto){
        // 1. 만들 수 있는 직위인지 검증
        Evaluations evaluation = evaluationsService.findById(evaluationId);
        MemberDepartment department = evaluation.getDepartment();

        if(!(department.equals(loginMember.getDepartment())
                && loginMember.getPosition().equals(MemberPosition.MANAGER))
                && !loginMember.getPosition().equals(MemberPosition.CEO)) {
            throw new QuestionsCustomException(
                    QuestionsExceptionCode.NOT_PERMISSION_DEPARTMENT_OR_POSITION);
        }

        List<QuestionsResponseDto.CreateOneDto> responseDtoList = new ArrayList<>();
        Integer questionsNumber = 1;

        // 2. 질문을 만드는데 알맞는 형태인지?
        for(QuestionsRequestDto.CreateOneDto requestQuestion : requestDto.getQuestionList()){
            // 평가 -> 질문 -> 답
            QuestionsType questionsType
                    = QuestionsConverter.toQuestionsType(requestQuestion.getQuestionType());
            Questions question
                    = QuestionsConverter.toEntity(
                    evaluation, questionsType, requestQuestion.getQuestionText(), (long) questionsNumber);

            answersService.createAll(question, requestQuestion);

            questionsRepository.save(question);
            evaluation.addQuestion(question);
            responseDtoList.add(
                    QuestionsConverter.toCreateOneDto(
                            question.getQuestionText(), question.getQuestionsType(),
                            questionsNumber));
            questionsNumber++;
        }

        return QuestionsConverter.toCreateAllDto(
                evaluation.getTitle(), evaluation.getYear(),
                evaluation.getQuarter(), responseDtoList);
    }

    @Override
    @Transactional
    public QuestionsResponseDto.UpdateOneDto updateOne(
            Long evaluationId, Long questionId,
            Members loginMember, QuestionsRequestDto.UpdateOneDto requestDto){
        // 1. 존재하는 평가인지?
        Evaluations evaluation = evaluationsService.findById(evaluationId);
        Questions question = findById(questionId);

        if(!loginMember.getDepartment().equals(evaluation.getDepartment())
                && loginMember.getPosition().equals(MemberPosition.MANAGER)){
            throw new QuestionsCustomException(QuestionsExceptionCode.NOT_PERMISSION_DEPARTMENT_OR_POSITION);
        }

        QuestionsType questionsType
                = QuestionsConverter.toQuestionsType(requestDto.getChangeQuestionType());
        if(requestDto.getMultipleChoiceAnswerList().size() > 1){
            List<Answers> updateMultipleChoiceAnswerList
                    = answersService.updateAllForMultipleChoiceAnswer(question, requestDto.getMultipleChoiceAnswerList());
            question.updateForMultipleChoiceAnswerList(
                    requestDto.getChangeQuestionText(), questionsType, updateMultipleChoiceAnswerList);
        }else if(requestDto.getMultipleChoiceAnswerList().isEmpty()){
            answersService.updateAllForShortAnswer(questionId);
            question.updateForShortAnswer(
                    requestDto.getChangeQuestionText(), questionsType);
        }else{
            // 질문 1개에 대한 응답 번호가 한 개 일 때
            throw new QuestionsCustomException(QuestionsExceptionCode.NEED_EXCEED_ONE_ANSWER_OR_NULL);
        }

        return QuestionsConverter.toUpdateOneDto(question);
    }

    @Override
    @Transactional
    public QuestionsResponseDto.UpdateOneForOrderDto updateOneForChangedOrder(
            Long evaluationId, Long questionId,
            Members loginMember, QuestionsRequestDto.UpdateOneForOrderDto requestDto){
        Evaluations evaluation = evaluationsService.findById(evaluationId);
        Questions question = findById(questionId);

        List<Questions> evaluationQuestionList = evaluation.getQuestionList();
        Long maxOrderSize = (long) evaluationQuestionList.size();
        if(requestDto.getUpdatedNumber() > maxOrderSize){
            throw new QuestionsCustomException(QuestionsExceptionCode. ORDER_EXCEEDS_MAX_SIZE);
        }

        Long beforeUpdatedNumber = question.getNumber();
        if(beforeUpdatedNumber.equals(requestDto.getUpdatedNumber())){
            throw new QuestionsCustomException(QuestionsExceptionCode.MATCHED_BEFORE_AND_AFTER_ORDER);
        }

        // 순서 변경
        List<Questions> changedEvaluationQuestionList
                = swapOrder(evaluationQuestionList, beforeUpdatedNumber, requestDto.getUpdatedNumber());
        // 상위 도메인에도 바뀐 내용 전달
        evaluation.updateQuestionList(changedEvaluationQuestionList);
        // 변경되어질 question 도메인 찾기
        Questions updatedQuestion = findByOrder(beforeUpdatedNumber);
        // question 2개 변경
        question.updateForChangedOrder(requestDto.getUpdatedNumber());
        updatedQuestion.updateForChangedOrder(beforeUpdatedNumber);

        return QuestionsConverter.toUpdateOneForChangedOrderDto(
                beforeUpdatedNumber, requestDto.getUpdatedNumber(),
                question.getQuestionText(), question.getQuestionsType(),
                question.getMultipleChoiceAnswerList());
    }

    @Override
    @Transactional
    public void delete(
            Long evaluationId, Members loginMember,
            QuestionsRequestDto.DeleteDto requestDto){
        // 1. 삭제할 권한 체크
        Evaluations evaluation = evaluationsService.findById(evaluationId);
        // 2. 삭제할 아이디 없으면 오류
        if(requestDto.getQuestionNumberList().isEmpty()){
            throw new QuestionsCustomException(QuestionsExceptionCode.NOT_INPUT_DELETED_QUESTION);
        }
        // 3. 삭제된다면 해당 평가의 질문 리스트의 순서를 변경해야함
        List<Long> deletedQuestionNumberList = requestDto.getQuestionNumberList();
        for(int i = 0; i < deletedQuestionNumberList.size(); i++){
            // 질문 10개 삭제될 질문이 2-5이라고 가정
            // 삭제할 질문이 존재하는지 확인
            questionsRepository.findByEvaluationIdAndNumber(
                    evaluationId, deletedQuestionNumberList.get(i))
                    .orElseThrow(()-> new QuestionsCustomException(
                            QuestionsExceptionCode.NOT_FOUND_QUESTIONS_ORDER));
        }
        // 해당 부분에 삭제된 질문들의 순서 공백을 채워넣어야함
        // 상위 도메인 변경 사항
        List<Questions> notDeletedEvaluationQuestionList
                = questionsRepository.findAllByNumberNotInAndEvaluationId(
                        requestDto.getQuestionNumberList(), evaluationId);
        List<Questions> changedQuestionList = updateOrder(notDeletedEvaluationQuestionList);
        evaluation.updateQuestionList(changedQuestionList);

        // 하위 도메인 변경 사항
        int index = 0;
        for(Questions question : changedQuestionList){
            question.updateForChangedOrder(changedQuestionList.get(index).getNumber());
            index++;
        }

        // 요청한 질문 삭제
        for(Long deletedNumber : requestDto.getQuestionNumberList()){
            questionsRepository.deleteByEvaluationIdAndNumber(evaluationId,deletedNumber);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Questions findById(Long questionId){
        return questionsRepository.findById(questionId).orElseThrow(
                ()-> new QuestionsCustomException(QuestionsExceptionCode.NOT_FOUND_QUESTION));
    }

    @Transactional(readOnly = true)
    public Questions findByOrder(Long number){
        return questionsRepository.findByNumber(number).orElseThrow(
                ()-> new QuestionsCustomException(QuestionsExceptionCode.NOT_FOUND_QUESTIONS_ORDER)
        );
    }

    private List<Questions> swapOrder(List<Questions> evaluationQuestionList, Long beforeChangedNumber, Long updatedNumber) {
        int beforeIndex = beforeChangedNumber.intValue() - 1;
        int afterIndex = updatedNumber.intValue() - 1;

        Questions questionToMove = evaluationQuestionList.get(beforeIndex);
        evaluationQuestionList.remove(beforeIndex);

        if (beforeIndex < afterIndex) {
            evaluationQuestionList.add(afterIndex, questionToMove);
        } else {
            evaluationQuestionList.add(afterIndex, questionToMove);
        }
        return evaluationQuestionList;
    }

    private List<Questions> updateOrder(List<Questions> notDeletedEvaluationQuestionList){
        // notDeletedEvaluationQuestionList -> 질문을 가지게 됨 -> 1,6,7,8,9,10
        // 해당하는 리스트의 순서를 땡겨와야함
        List<Questions> changedEvaluationQuestionList = new ArrayList<>();
        for(int index = 0; index<notDeletedEvaluationQuestionList.size(); index++){
            Long changedNumber = index + 1L;
            if(!notDeletedEvaluationQuestionList.get(index).getNumber().equals(changedNumber)){
                // 다르면
                Questions changedQuestion
                        = QuestionsConverter.toUpdateOneForChangedOrder(
                                changedEvaluationQuestionList.get(index), changedNumber);
                changedEvaluationQuestionList.add(changedQuestion);
            }else{
                // 같으면
                changedEvaluationQuestionList.add(notDeletedEvaluationQuestionList.get(index));
            }
        }
        return changedEvaluationQuestionList;
    }
}
