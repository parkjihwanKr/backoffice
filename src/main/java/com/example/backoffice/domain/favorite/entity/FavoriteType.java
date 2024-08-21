package com.example.backoffice.domain.favorite.entity;

import lombok.Getter;

@Getter
public enum FavoriteType {
    BOARD(DomainLabel.BOARD),
    EVENT(DomainLabel.EVENT),
    COMMENT(DomainLabel.COMMENT),
    REPLY(DomainLabel.REPLY);

    private final String domainType;

    FavoriteType(String domainType) {
        this.domainType = domainType;
    }

    public static class DomainLabel {
        private static final String BOARD = "FAVORITE_BOARD";
        private static final String EVENT = "FAVORITE_EVENT";
        private static final String COMMENT = "FAVORITE_COMMENT";
        private static final String REPLY = "FAVORITE_REPLY";
    }
}
