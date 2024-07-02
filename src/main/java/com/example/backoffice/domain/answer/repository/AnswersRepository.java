package com.example.backoffice.domain.answer.repository;

import com.example.backoffice.domain.answer.entity.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswersRepository extends JpaRepository<Answers, Long> {
}
