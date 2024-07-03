package com.example.backoffice.domain.question.repository;

import com.example.backoffice.domain.question.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    List<Questions> findAllByEvaluationId(Long evaluationId);
}
