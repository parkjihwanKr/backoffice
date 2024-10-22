package com.example.backoffice.domain.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AssetResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneDto{
        private Long assetId;
        private Double totalBalance;
        // 부서별 지출 상황 기록 필드 추가
    }
}
