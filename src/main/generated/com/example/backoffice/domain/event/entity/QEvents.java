package com.example.backoffice.domain.event.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvents is a Querydsl query type for Events
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvents extends EntityPathBase<Events> {

    private static final long serialVersionUID = 412390496L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvents events = new QEvents("events");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.example.backoffice.domain.member.entity.MemberDepartment> department = createEnum("department", com.example.backoffice.domain.member.entity.MemberDepartment.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final EnumPath<EventType> eventType = createEnum("eventType", EventType.class);

    public final ListPath<com.example.backoffice.domain.file.entity.Files, com.example.backoffice.domain.file.entity.QFiles> fileList = this.<com.example.backoffice.domain.file.entity.Files, com.example.backoffice.domain.file.entity.QFiles>createList("fileList", com.example.backoffice.domain.file.entity.Files.class, com.example.backoffice.domain.file.entity.QFiles.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.backoffice.domain.member.entity.QMembers member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public QEvents(String variable) {
        this(Events.class, forVariable(variable), INITS);
    }

    public QEvents(Path<? extends Events> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvents(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvents(PathMetadata metadata, PathInits inits) {
        this(Events.class, metadata, inits);
    }

    public QEvents(Class<? extends Events> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
    }

}

