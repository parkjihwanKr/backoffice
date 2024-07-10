package com.example.backoffice.domain.evaluation.repository;

import com.example.backoffice.domain.evaluation.entity.EvaluationType;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationsRepository extends JpaRepository<Evaluations, Long>{

    Optional<Evaluations> findByIdAndEvaluationType(
            Long evaluationId, EvaluationType evaluationType);
}
