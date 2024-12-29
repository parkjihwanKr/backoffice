package com.example.backoffice.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMembers is a Querydsl query type for Members
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMembers extends EntityPathBase<Members> {

    private static final long serialVersionUID = -1521908014L;

    public static final QMembers members = new QMembers("members");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    public final StringPath address = createString("address");

    public final ListPath<com.example.backoffice.domain.attendance.entity.Attendances, com.example.backoffice.domain.attendance.entity.QAttendances> attendanceList = this.<com.example.backoffice.domain.attendance.entity.Attendances, com.example.backoffice.domain.attendance.entity.QAttendances>createList("attendanceList", com.example.backoffice.domain.attendance.entity.Attendances.class, com.example.backoffice.domain.attendance.entity.QAttendances.class, PathInits.DIRECT2);

    public final StringPath contact = createString("contact");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<MemberDepartment> department = createEnum("department", MemberDepartment.class);

    public final StringPath email = createString("email");

    public final ListPath<com.example.backoffice.domain.event.entity.Events, com.example.backoffice.domain.event.entity.QEvents> eventList = this.<com.example.backoffice.domain.event.entity.Events, com.example.backoffice.domain.event.entity.QEvents>createList("eventList", com.example.backoffice.domain.event.entity.Events.class, com.example.backoffice.domain.event.entity.QEvents.class, PathInits.DIRECT2);

    public final ListPath<com.example.backoffice.domain.favorite.entity.Favorites, com.example.backoffice.domain.favorite.entity.QFavorites> favoritieList = this.<com.example.backoffice.domain.favorite.entity.Favorites, com.example.backoffice.domain.favorite.entity.QFavorites>createList("favoritieList", com.example.backoffice.domain.favorite.entity.Favorites.class, com.example.backoffice.domain.favorite.entity.QFavorites.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final NumberPath<Long> loveCount = createNumber("loveCount", Long.class);

    public final StringPath memberName = createString("memberName");

    public final ListPath<com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations, com.example.backoffice.domain.memberEvaluation.entity.QMembersEvaluations> membersEvaluations = this.<com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations, com.example.backoffice.domain.memberEvaluation.entity.QMembersEvaluations>createList("membersEvaluations", com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations.class, com.example.backoffice.domain.memberEvaluation.entity.QMembersEvaluations.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final BooleanPath onVacation = createBoolean("onVacation");

    public final StringPath password = createString("password");

    public final EnumPath<MemberPosition> position = createEnum("position", MemberPosition.class);

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final NumberPath<Integer> remainingVacationDays = createNumber("remainingVacationDays", Integer.class);

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public final NumberPath<Long> salary = createNumber("salary", Long.class);

    public QMembers(String variable) {
        super(Members.class, forVariable(variable));
    }

    public QMembers(Path<? extends Members> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMembers(PathMetadata metadata) {
        super(Members.class, metadata);
    }

}

