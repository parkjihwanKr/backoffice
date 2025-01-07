package com.example.backoffice.domain.memberAnswer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMembersAnswers is a Querydsl query type for MembersAnswers
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembersAnswers extends EntityPathBase<MembersAnswers> {

    private static final long serialVersionUID = -131630303L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMembersAnswers membersAnswers = new QMembersAnswers("membersAnswers");

    public final com.example.backoffice.domain.answer.entity.QAnswers answer;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.backoffice.domain.member.entity.QMembers member;

    public final com.example.backoffice.domain.question.entity.QQuestions question;

    public QMembersAnswers(String variable) {
        this(MembersAnswers.class, forVariable(variable), INITS);
    }

    public QMembersAnswers(Path<? extends MembersAnswers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMembersAnswers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMembersAnswers(PathMetadata metadata, PathInits inits) {
        this(MembersAnswers.class, metadata, inits);
    }

    public QMembersAnswers(Class<? extends MembersAnswers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.answer = inits.isInitialized("answer") ? new com.example.backoffice.domain.answer.entity.QAnswers(forProperty("answer"), inits.get("answer")) : null;
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
        this.question = inits.isInitialized("question") ? new com.example.backoffice.domain.question.entity.QQuestions(forProperty("question"), inits.get("question")) : null;
    }

}

