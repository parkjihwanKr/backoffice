package com.example.backoffice.domain.evaluation.service;

import com.example.backoffice.domain.evaluation.entity.EvaluationType;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsExceptionCode;
import com.example.backoffice.domain.evaluation.repository.EvaluationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EvaluationsServiceV1Impl implements EvaluationsServiceV1{

    private final EvaluationsRepository evaluationsRepository;

    @Override
    @Transactional
    public void save(Evaluations evaluation){
        evaluationsRepository.save(evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    public Evaluations findById(Long evaluationId){
        return evaluationsRepository.findById(evaluationId).orElseThrow(
                ()-> new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATIONS));
    }

    @Override
    @Transactional
    public void deleteById(Long evaluationId){
        evaluationsRepository.deleteById(evaluationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Evaluations findByIdAndEvaluationType(
            Long evaluationId, EvaluationType evaluationType){
        return evaluationsRepository.findByIdAndEvaluationType(evaluationId, evaluationType).orElseThrow(
                ()-> new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATIONS)
        );
    }
}
