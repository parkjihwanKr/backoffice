package com.example.backoffice.domain.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestions is a Querydsl query type for Questions
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestions extends EntityPathBase<Questions> {

    private static final long serialVersionUID = -1713602310L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestions questions = new QQuestions("questions");

    public final com.example.backoffice.domain.evaluation.entity.QEvaluations evaluation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.backoffice.domain.answer.entity.Answers, com.example.backoffice.domain.answer.entity.QAnswers> multipleChoiceAnswerList = this.<com.example.backoffice.domain.answer.entity.Answers, com.example.backoffice.domain.answer.entity.QAnswers>createList("multipleChoiceAnswerList", com.example.backoffice.domain.answer.entity.Answers.class, com.example.backoffice.domain.answer.entity.QAnswers.class, PathInits.DIRECT2);

    public final NumberPath<Long> number = createNumber("number", Long.class);

    public final EnumPath<QuestionsType> questionsType = createEnum("questionsType", QuestionsType.class);

    public final StringPath questionText = createString("questionText");

    public QQuestions(String variable) {
        this(Questions.class, forVariable(variable), INITS);
    }

    public QQuestions(Path<? extends Questions> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestions(PathMetadata metadata, PathInits inits) {
        this(Questions.class, metadata, inits);
    }

    public QQuestions(Class<? extends Questions> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.evaluation = inits.isInitialized("evaluation") ? new com.example.backoffice.domain.evaluation.entity.QEvaluations(forProperty("evaluation")) : null;
    }

}

