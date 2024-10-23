package com.example.backoffice.domain.asset.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.backoffice.domain.asset.converter.AssetConverter;
import com.example.backoffice.domain.asset.dto.AssetResponseDto;
import com.example.backoffice.domain.asset.entity.Asset;
import com.example.backoffice.domain.asset.repository.AssetRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssetServiceImplV1 implements AssetServiceV1{

    private final MembersServiceV1 membersService;
    private final AssetRepository assetRepository;

    @Override
    @Transactional(readOnly = true)
    public AssetResponseDto.ReadOneDto readOne(Members loginMember){
        // 1. 권한 확인
        membersService.findByFinanceManagerOrCeo(loginMember.getId());

        // 2. 조회
        Asset asset = assetRepository.findById(1L).orElseThrow(
                ()->  new NotFoundException("못 찾음"));

        return AssetConverter.toReadOneDto(asset);
    }
}
