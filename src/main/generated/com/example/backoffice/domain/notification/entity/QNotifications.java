package com.example.backoffice.domain.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotifications is a Querydsl query type for Notifications
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotifications extends EntityPathBase<Notifications> {

    private static final long serialVersionUID = 399812144L;

    public static final QNotifications notifications = new QNotifications("notifications");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.example.backoffice.domain.member.entity.MemberDepartment> fromMemberDepartment = createEnum("fromMemberDepartment", com.example.backoffice.domain.member.entity.MemberDepartment.class);

    public final StringPath fromMemberName = createString("fromMemberName");

    public final StringPath id = createString("id");

    public final BooleanPath isRead = createBoolean("isRead");

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<NotificationType> notificationType = createEnum("notificationType", NotificationType.class);

    public final StringPath toMemberName = createString("toMemberName");

    public QNotifications(String variable) {
        super(Notifications.class, forVariable(variable));
    }

    public QNotifications(Path<? extends Notifications> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotifications(PathMetadata metadata) {
        super(Notifications.class, metadata);
    }

}

