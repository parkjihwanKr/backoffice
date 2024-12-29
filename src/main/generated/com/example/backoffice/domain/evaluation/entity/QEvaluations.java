package com.example.backoffice.domain.evaluation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvaluations is a Querydsl query type for Evaluations
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvaluations extends EntityPathBase<Evaluations> {

    private static final long serialVersionUID = -874725874L;

    public static final QEvaluations evaluations = new QEvaluations("evaluations");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.example.backoffice.domain.member.entity.MemberDepartment> department = createEnum("department", com.example.backoffice.domain.member.entity.MemberDepartment.class);

    public final StringPath description = createString("description");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final EnumPath<EvaluationType> evaluationType = createEnum("evaluationType", EvaluationType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations, com.example.backoffice.domain.memberEvaluation.entity.QMembersEvaluations> membersEvaluations = this.<com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations, com.example.backoffice.domain.memberEvaluation.entity.QMembersEvaluations>createList("membersEvaluations", com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations.class, com.example.backoffice.domain.memberEvaluation.entity.QMembersEvaluations.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> quarter = createNumber("quarter", Integer.class);

    public final ListPath<com.example.backoffice.domain.question.entity.Questions, com.example.backoffice.domain.question.entity.QQuestions> questionList = this.<com.example.backoffice.domain.question.entity.Questions, com.example.backoffice.domain.question.entity.QQuestions>createList("questionList", com.example.backoffice.domain.question.entity.Questions.class, com.example.backoffice.domain.question.entity.QQuestions.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QEvaluations(String variable) {
        super(Evaluations.class, forVariable(variable));
    }

    public QEvaluations(Path<? extends Evaluations> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEvaluations(PathMetadata metadata) {
        super(Evaluations.class, metadata);
    }

}

