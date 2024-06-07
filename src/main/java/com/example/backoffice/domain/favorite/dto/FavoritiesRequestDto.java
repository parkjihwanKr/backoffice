package com.example.backoffice.domain.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FavoritiesRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFavoriteRequestDto {
        private String targetType;
        private Long targetId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteFavoriteIdListRequestDto {
        private List<Long> favoriteIdList;
    }
}
