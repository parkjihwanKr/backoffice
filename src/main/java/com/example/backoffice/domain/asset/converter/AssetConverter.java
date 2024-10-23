package com.example.backoffice.domain.asset.converter;

import com.example.backoffice.domain.asset.dto.AssetResponseDto;
import com.example.backoffice.domain.asset.entity.Asset;

public class AssetConverter {

    public static AssetResponseDto.ReadOneDto toReadOneDto(Asset asset){
        return AssetResponseDto.ReadOneDto.builder()
                .assetId(asset.getId())
                .totalBalance(asset.getTotalBalance())
                .build();
    }
}
