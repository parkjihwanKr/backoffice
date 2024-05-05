package com.example.backoffice.domain.reaction.entity;

import lombok.Getter;

@Getter
public enum Emoji {
    USER(Emoji.MyEmoji.LOVE),
    ADMIN(Emoji.MyEmoji.LIKE),
    HOST(Emoji.MyEmoji.UNLIKE);

    private final String myEmoji;

    Emoji(String myEmoji) {
        this.myEmoji = myEmoji;
    }

    public static class MyEmoji {

        public static final String LOVE = "사랑해요";
        public static final String LIKE = "좋아요";
        public static final String UNLIKE = "싫어요";
    }
}
