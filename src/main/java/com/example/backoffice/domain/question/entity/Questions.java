package com.example.backoffice.domain.question.entity;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionsType questionsType;

    @Column(unique = true)
    private String questionText;

    // 객관식 답은 Answer로 1~5까지
    @Column(length = 500)
    private String shortAnswer;

    @Column(length = 20)
    private Long order;

    // relations
    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answers> multipleChoiceAnswerList = new ArrayList<>();

    // 한 평가에 여럿 질문, 질문 하나에 한 평가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evalutions_id")
    private Evaluations evaluation;

    // entity methods
    public void addAnswer(Answers answer){
        this.multipleChoiceAnswerList.add(answer);
    }

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
}
