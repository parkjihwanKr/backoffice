package com.example.backoffice.domain.board.entity;

import lombok.Getter;

@Getter
public enum BoardType {
    GENERAL(BoardTypeLabels.GENERAL),
    DEPARTMENT(BoardTypeLabels.DEPARTMENT);

    private final String label;

    BoardType(String label) {
        this.label = label;
    }

    public static class BoardTypeLabels {
        public static final String GENERAL = "GENERAL";
        public static final String DEPARTMENT = "DEPARTMENT";
    }
}
