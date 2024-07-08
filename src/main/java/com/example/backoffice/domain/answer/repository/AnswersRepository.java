package com.example.backoffice.domain.answer.repository;

import com.example.backoffice.domain.answer.entity.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersRepository extends JpaRepository<Answers, Long> {

    List<Answers> findAllByQuestionId(Long questionId);

    void deleteByText(String answerText);
}
