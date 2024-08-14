package com.example.backoffice.domain.memberEvaluation.service;

import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsExceptionCode;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.memberEvaluation.exception.MembersEvaluationsCustomException;
import com.example.backoffice.domain.memberEvaluation.exception.MembersEvaluationsExceptionCode;
import com.example.backoffice.domain.memberEvaluation.repository.MembersEvaluationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembersEvaluationsServiceImplV1 implements MembersEvaluationsServiceV1{

    private final MembersEvaluationsRepository membersEvaluationsRepository;

    @Override
    @Transactional(readOnly = true)
    public MembersEvaluations findByMemberIdAndEvaluationId(Long memberId, Long evaluationId){
        return membersEvaluationsRepository.findByMemberIdAndEvaluationId(memberId, evaluationId).orElseThrow(
                ()-> new MembersEvaluationsCustomException(
                        MembersEvaluationsExceptionCode.NOT_FOUND_MEMBERS_EVALUATIONS)
        );
    }

    @Override
    @Transactional
    public void save(MembersEvaluations membersEvaluations){
        membersEvaluationsRepository.save(membersEvaluations);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembersEvaluations> findAllByIsCompleted(Boolean isCompleted){
        return membersEvaluationsRepository.findAllByIsCompleted(isCompleted);
    }

    @Override
    @Transactional
    public void deleteSubmittedOneForCancellation(Long evaluationId, Long loginMemberId){
        MembersEvaluations memberEvaluation
                = findByMemberIdAndEvaluationId(evaluationId, loginMemberId);

        // 할당된 설문조사를 참여 했는지?
        if(!memberEvaluation.getIsCompleted()){
            throw new MembersEvaluationsCustomException(MembersEvaluationsExceptionCode.NOT_COMPLETED_EVALUATION);
        }

        memberEvaluation.unCompleted(false, null);
    }
}
