package com.example.backoffice.domain.memberEvaluation.service;

import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsExceptionCode;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.memberEvaluation.repository.MembersEvaluationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MembersEvaluationsServiceImplV1 implements MembersEvaluationsServiceV1{

    private final MembersEvaluationsRepository membersEvaluationsRepository;

    @Override
    @Transactional(readOnly = true)
    public MembersEvaluations findByMemberIdAndEvaluationId(Long memberId, Long evaluationId){
        return membersEvaluationsRepository.findByMemberIdAndEvaluationId(memberId, evaluationId).orElseThrow(
                ()-> new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATIONS)
        );
    }

    @Override
    @Transactional
    public void save(MembersEvaluations membersEvaluations){
        membersEvaluationsRepository.save(membersEvaluations);
    }
}
