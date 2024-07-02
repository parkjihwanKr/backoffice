package com.example.backoffice.domain.answer.entity;

import com.example.backoffice.domain.question.entity.Questions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Answers")
public class Answers {

    // fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Questions question;

    // 예시 1. 회사 생활에 만족하십니까?
    // 1. 매우 만족 ~ 5. 매우 불만족
    // 예시 2. 회사 생활에 불만족하시다면 그 이유를 설명해주세요
    // 이러한 이유로 회사가 불만족스럽습니다.
    private String answerText;

    // entity methods
}
