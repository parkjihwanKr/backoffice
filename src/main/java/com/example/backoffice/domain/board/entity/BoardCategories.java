package com.example.backoffice.domain.board.entity;

import lombok.Getter;

@Getter
public enum BoardCategories {

    COMMUNICATIONS(BoardCategoryLabels.COMMUNICATIONS),
    NOTIFICATIONS(BoardCategoryLabels.NOTIFICATIONS),
    AUDITORIUM(BoardCategoryLabels.AUDITORIUM),
    ;

    private final String label;

    BoardCategories(String label) {
        this.label = label;
    }

    public static class BoardCategoryLabels {
        public static final String COMMUNICATIONS = "협업";
        public static final String NOTIFICATIONS = "전체 알림";
        public static final String AUDITORIUM = "회의실";
    }
}
