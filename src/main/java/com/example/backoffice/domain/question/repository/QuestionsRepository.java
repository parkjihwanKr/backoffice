package com.example.backoffice.domain.question.repository;

import com.example.backoffice.domain.question.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    Optional<Questions> findByOrder(Long order);
    Optional<Questions> findByEvaluationIdAndOrder(Long evaluationId, Long order);
    List<Questions> findAllByOrderNotInAndEvaluationId(List<Long> deletedOrderList, Long evaluationId);
    void deleteByEvaluationIdAndOrder(Long evaluationId, Long deletedOrder);
}
