package com.example.backoffice.domain.asset.repository;

import com.example.backoffice.domain.asset.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}
