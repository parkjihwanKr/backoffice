package com.example.backoffice.domain.evaluation.repository;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationsRepository extends JpaRepository<Evaluations, Long>{
}
