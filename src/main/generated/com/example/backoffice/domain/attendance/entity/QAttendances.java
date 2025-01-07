package com.example.backoffice.domain.attendance.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttendances is a Querydsl query type for Attendances
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttendances extends EntityPathBase<Attendances> {

    private static final long serialVersionUID = -1302168780L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttendances attendances = new QAttendances("attendances");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    public final EnumPath<AttendanceStatus> attendanceStatus = createEnum("attendanceStatus", AttendanceStatus.class);

    public final DateTimePath<java.time.LocalDateTime> checkInTime = createDateTime("checkInTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> checkOutTime = createDateTime("checkOutTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.backoffice.domain.member.entity.QMembers member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QAttendances(String variable) {
        this(Attendances.class, forVariable(variable), INITS);
    }

    public QAttendances(Path<? extends Attendances> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttendances(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttendances(PathMetadata metadata, PathInits inits) {
        this(Attendances.class, metadata, inits);
    }

    public QAttendances(Class<? extends Attendances> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
    }

}

