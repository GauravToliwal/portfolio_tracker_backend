package com.wealthtracker.backend.service;

import com.wealthtracker.backend.dto.asset.AssetRequest;
import com.wealthtracker.backend.dto.asset.AssetResponse;
import com.wealthtracker.backend.entity.Asset;
import com.wealthtracker.backend.exception.ResourceNotFoundException;
import com.wealthtracker.backend.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final CurrentUserService currentUserService;

    public List<AssetResponse> getAllAssets() {
        Long userId = currentUserService.getCurrentUser().getId();
        return assetRepository.findAllByUserIdOrderByValuationDateDesc(userId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public AssetResponse createAsset(AssetRequest request) {
        Asset asset = new Asset();
        apply(asset, request);
        asset.setUser(currentUserService.getCurrentUser());
        return toResponse(assetRepository.save(asset));
    }

    public AssetResponse updateAsset(Long assetId, AssetRequest request) {
        Asset asset = findOwnedAsset(assetId);
        apply(asset, request);
        return toResponse(assetRepository.save(asset));
    }

    public void deleteAsset(Long assetId) {
        assetRepository.delete(findOwnedAsset(assetId));
    }

    private Asset findOwnedAsset(Long assetId) {
        Long currentUserId = currentUserService.getCurrentUser().getId();
        Asset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        if (!asset.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Asset not found");
        }
        return asset;
    }

    private void apply(Asset asset, AssetRequest request) {
        asset.setName(request.name());
        asset.setInstitution(request.institution());
        asset.setAssetType(request.assetType());
        asset.setQuantity(request.quantity());
        asset.setInvestedValue(request.investedValue());
        asset.setCurrentValue(request.currentValue());
        asset.setValuationDate(request.valuationDate());
    }

    private AssetResponse toResponse(Asset asset) {
        return new AssetResponse(
            asset.getId(),
            asset.getName(),
            asset.getInstitution(),
            asset.getAssetType(),
            asset.getQuantity(),
            asset.getInvestedValue(),
            asset.getCurrentValue(),
            asset.getValuationDate()
        );
    }
}
