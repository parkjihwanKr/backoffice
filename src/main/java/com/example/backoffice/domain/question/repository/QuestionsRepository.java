package com.example.backoffice.domain.question.repository;

import com.example.backoffice.domain.question.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    Optional<Questions> findByNumber(Long number);
    Optional<Questions> findByEvaluationIdAndNumber(Long evaluationId, Long number);
    List<Questions> findAllByNumberNotInAndEvaluationId(List<Long> deletedNumberList, Long evaluationId);
    void deleteByEvaluationIdAndNumber(Long evaluationId, Long deletedNumber);
}
