package com.example.backoffice.domain.reaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReactions is a Querydsl query type for Reactions
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReactions extends EntityPathBase<Reactions> {

    private static final long serialVersionUID = -199427276L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReactions reactions = new QReactions("reactions");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    public final com.example.backoffice.domain.board.entity.QBoards board;

    public final com.example.backoffice.domain.comment.entity.QComments comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<Emoji> emoji = createEnum("emoji", Emoji.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.backoffice.domain.member.entity.QMembers member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.example.backoffice.domain.member.entity.QMembers reactor;

    public final com.example.backoffice.domain.comment.entity.QComments reply;

    public QReactions(String variable) {
        this(Reactions.class, forVariable(variable), INITS);
    }

    public QReactions(Path<? extends Reactions> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReactions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReactions(PathMetadata metadata, PathInits inits) {
        this(Reactions.class, metadata, inits);
    }

    public QReactions(Class<? extends Reactions> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.example.backoffice.domain.board.entity.QBoards(forProperty("board"), inits.get("board")) : null;
        this.comment = inits.isInitialized("comment") ? new com.example.backoffice.domain.comment.entity.QComments(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
        this.reactor = inits.isInitialized("reactor") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("reactor")) : null;
        this.reply = inits.isInitialized("reply") ? new com.example.backoffice.domain.comment.entity.QComments(forProperty("reply"), inits.get("reply")) : null;
    }

}

