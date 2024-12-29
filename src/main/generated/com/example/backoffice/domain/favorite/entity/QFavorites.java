package com.example.backoffice.domain.favorite.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavorites is a Querydsl query type for Favorites
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavorites extends EntityPathBase<Favorites> {

    private static final long serialVersionUID = 280067854L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavorites favorites = new QFavorites("favorites");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath favoritesUrl = createString("favoritesUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.backoffice.domain.member.entity.QMembers member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QFavorites(String variable) {
        this(Favorites.class, forVariable(variable), INITS);
    }

    public QFavorites(Path<? extends Favorites> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavorites(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavorites(PathMetadata metadata, PathInits inits) {
        this(Favorites.class, metadata, inits);
    }

    public QFavorites(Class<? extends Favorites> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.backoffice.domain.member.entity.QMembers(forProperty("member")) : null;
    }

}

