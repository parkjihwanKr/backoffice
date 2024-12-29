package com.example.backoffice.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoards is a Querydsl query type for Boards
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoards extends EntityPathBase<Boards> {

    private static final long serialVersionUID = -268523296L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoards boards = new QBoards("boards");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    public final EnumPath<BoardType> boardType = createEnum("boardType", BoardType.class);

    public final EnumPath<BoardCategories> categories = createEnum("categories", BoardCategories.class);

    public final ListPath<com.example.backoffice.domain.comment.entity.Comments, com.example.backoffice.domain.comment.entity.QComments> commentList = this.<com.example.backoffice.domain.comment.entity.Comments, com.example.backoffice.domain.comment.entity.QComments>createList("commentList", com.example.backoffice.domain.comment.entity.Comments.class, com.example.backoffice.domain.comment.entity.QComments.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.example.backoffice.domain.member.entity.MemberDepartment> department = createEnum("department", com.example.backoffice.domain.member.entity.MemberDepartment.class);

    public final ListPath<com.example.backoffice.domain.file.entity.Files, com.example.backoffice.domain.file.entity.QFiles> fileList = this.<com.example.backoffice.domain.file.entity.Files, com.example.backoffice.domain.file.entity.QFiles>createList("fileList", com.example.backoffice.domain.file.entity.Files.class, com.example.backoffice.domain.file.entity.QFiles.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isImportant = createBoolean("isImportant");

    public final BooleanPath isLocked = createBoolean("isLocked");

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final com.example.backoffice.domain.member.entity.QMembers member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<com.example.backoffice.domain.reaction.entity.Reactions, com.example.backoffice.domain.reaction.entity.QReactions> reactionList = this.<com.example.backoffice.domain.reaction.entity.Reactions, com.example.backoffice.domain.reaction.entity.QReactions>createList("reactionList", com.example.backoffice.domain.reaction.entity.Reactions.class, com.example.backoffice.domain.reaction.entity.QReactions.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QBoards(String variable) {
        this(Boards.class, forVariable(variable), INITS);
    }

    public QBoards(Path<? extends Boards> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoards(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoards(PathMetadata metadata, PathInits inits) {
        this(Boards.class, metadata, inits);
    }

    public QBoards(Class<? extends Boards> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
    }

}

