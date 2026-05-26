package com.wealthtracker.backend.repository;

import com.wealthtracker.backend.entity.Asset;
import com.wealthtracker.backend.enums.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findAllByUserIdOrderByValuationDateDesc(Long userId);

    List<Asset> findAllByUserIdAndAssetType(Long userId, AssetType assetType);
}
