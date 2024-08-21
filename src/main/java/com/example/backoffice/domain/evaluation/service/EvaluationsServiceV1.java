package com.example.backoffice.domain.evaluation.service;

import com.example.backoffice.domain.evaluation.entity.EvaluationType;
import com.example.backoffice.domain.evaluation.entity.Evaluations;

import java.time.LocalDate;
import java.util.List;

public interface EvaluationsServiceV1 {

    void save(Evaluations evaluation);

    Evaluations findById(Long evaluationId);

    void deleteById(Long evaluationId);

    Evaluations findByIdAndEvaluationType(Long evaluationId, EvaluationType evaluationType);

    List<Evaluations> findAllByEndDatePlusSevenDays(LocalDate endDate);
}
