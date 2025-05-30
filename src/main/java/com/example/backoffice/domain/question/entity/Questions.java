package com.example.backoffice.domain.question.entity;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    //@NotNull
    @Enumerated(EnumType.STRING)
    private QuestionsType questionsType;

    @Column
    private String questionText;

    // order가 mysql 예약어라서 number로 수정
    private Long number;

    // relations
    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answers> multipleChoiceAnswerList = new ArrayList<>();

    // 한 평가에 여럿 질문, 질문 하나에 한 평가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evalutions_id")
    private Evaluations evaluation;

    // entity methods
    public void updateForMultipleChoiceAnswerList(
            String questionText, QuestionsType questionsType, List<Answers> newMultipleChoiceAnswerList){
        this.questionText = questionText;
        this.questionsType = questionsType;

        this.multipleChoiceAnswerList.clear();
        this.multipleChoiceAnswerList.addAll(newMultipleChoiceAnswerList);
    }

    public void updateForShortAnswer(String questionText, QuestionsType questionsType){
        this.questionText = questionText;
        this.questionsType = questionsType;
        this.multipleChoiceAnswerList.clear();
    }

    public void updateForChangedOrder(Long changedNumber){
        this.number = changedNumber;
    }
}
