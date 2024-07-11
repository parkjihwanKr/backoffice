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

    private String text;

    private Long number;

    // entity methods
    public void update(String text){
        this.text = text;
    }
}
