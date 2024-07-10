package com.example.backoffice.domain.evaluation.service;

import com.example.backoffice.domain.evaluation.entity.Evaluations;

public interface EvaluationsServiceV1 {

    void save(Evaluations evaluation);
    Evaluations findById(Long evaluationId);
    void deleteById(Long evaluationId);
}
