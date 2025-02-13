package com.example.backoffice.domain.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComments is a Querydsl query type for Comments
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComments extends EntityPathBase<Comments> {

    private static final long serialVersionUID = 721594688L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComments comments = new QComments("comments");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    public final com.example.backoffice.domain.board.entity.QBoards board;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final com.example.backoffice.domain.member.entity.QMembers member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QComments parent;

    public final ListPath<com.example.backoffice.domain.reaction.entity.Reactions, com.example.backoffice.domain.reaction.entity.QReactions> reactionList = this.<com.example.backoffice.domain.reaction.entity.Reactions, com.example.backoffice.domain.reaction.entity.QReactions>createList("reactionList", com.example.backoffice.domain.reaction.entity.Reactions.class, com.example.backoffice.domain.reaction.entity.QReactions.class, PathInits.DIRECT2);

    public final ListPath<Comments, QComments> replyList = this.<Comments, QComments>createList("replyList", Comments.class, QComments.class, PathInits.DIRECT2);

    public QComments(String variable) {
        this(Comments.class, forVariable(variable), INITS);
    }

    public QComments(Path<? extends Comments> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComments(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComments(PathMetadata metadata, PathInits inits) {
        this(Comments.class, metadata, inits);
    }

    public QComments(Class<? extends Comments> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.example.backoffice.domain.board.entity.QBoards(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
        this.parent = inits.isInitialized("parent") ? new QComments(forProperty("parent"), inits.get("parent")) : null;
    }

}

