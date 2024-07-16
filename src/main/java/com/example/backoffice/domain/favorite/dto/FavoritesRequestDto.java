package com.example.backoffice.domain.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FavoritesRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto {
        private String targetType;
        private Long targetId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteDto {
        private List<Long> favoriteIdList;
    }
}
