package com.example.backoffice.domain.answer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswers is a Querydsl query type for Answers
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswers extends EntityPathBase<Answers> {

    private static final long serialVersionUID = -1179429750L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswers answers = new QAnswers("answers");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> number = createNumber("number", Long.class);

    public final com.example.backoffice.domain.question.entity.QQuestions question;

    public final StringPath text = createString("text");

    public QAnswers(String variable) {
        this(Answers.class, forVariable(variable), INITS);
    }

    public QAnswers(Path<? extends Answers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswers(PathMetadata metadata, PathInits inits) {
        this(Answers.class, metadata, inits);
    }

    public QAnswers(Class<? extends Answers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new com.example.backoffice.domain.question.entity.QQuestions(forProperty("question"), inits.get("question")) : null;
    }

}

