package com.example.backoffice.domain.question.entity;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // relations
    // 한 평가에 여럿 질문, 질문 하나에 한 평가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evalutions_id")
    private Evaluations evaluation;

    // entity methods
}
