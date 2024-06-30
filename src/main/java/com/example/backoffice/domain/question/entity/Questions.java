package com.example.backoffice.domain.question.entity;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions")
public class Questions {

    // fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionsType questionsType;

    private String questionText;

    // 객관식 답은 Answer로 1~5까지

    // relations
    // 한 평가에 여럿 질문, 질문 하나에 한 평가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evalutions_id")
    private Evaluations evaluation;

    // entity methods
}
