package com.example.backoffice.domain.memberEvaluation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMembersEvaluations is a Querydsl query type for MembersEvaluations
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembersEvaluations extends EntityPathBase<MembersEvaluations> {

    private static final long serialVersionUID = -852373919L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMembersEvaluations membersEvaluations = new QMembersEvaluations("membersEvaluations");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    public final DateTimePath<java.time.LocalDateTime> completedDate = createDateTime("completedDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.example.backoffice.domain.evaluation.entity.QEvaluations evaluation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final com.example.backoffice.domain.member.entity.QMembers member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QMembersEvaluations(String variable) {
        this(MembersEvaluations.class, forVariable(variable), INITS);
    }

    public QMembersEvaluations(Path<? extends MembersEvaluations> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMembersEvaluations(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMembersEvaluations(PathMetadata metadata, PathInits inits) {
        this(MembersEvaluations.class, metadata, inits);
    }

    public QMembersEvaluations(Class<? extends MembersEvaluations> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.evaluation = inits.isInitialized("evaluation") ? new com.example.backoffice.domain.evaluation.entity.QEvaluations(forProperty("evaluation")) : null;
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
    }

}

