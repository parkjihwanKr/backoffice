package com.example.backoffice.domain.vacation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVacations is a Querydsl query type for Vacations
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVacations extends EntityPathBase<Vacations> {

    private static final long serialVersionUID = -1273004340L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVacations vacations = new QVacations("vacations");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAccepted = createBoolean("isAccepted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.example.backoffice.domain.member.entity.QMembers onVacationMember;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public final BooleanPath urgent = createBoolean("urgent");

    public final StringPath urgentReason = createString("urgentReason");

    public final EnumPath<VacationType> vacationType = createEnum("vacationType", VacationType.class);

    public QVacations(String variable) {
        this(Vacations.class, forVariable(variable), INITS);
    }

    public QVacations(Path<? extends Vacations> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVacations(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVacations(PathMetadata metadata, PathInits inits) {
        this(Vacations.class, metadata, inits);
    }

    public QVacations(Class<? extends Vacations> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.onVacationMember = inits.isInitialized("onVacationMember") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("onVacationMember")) : null;
    }

}

