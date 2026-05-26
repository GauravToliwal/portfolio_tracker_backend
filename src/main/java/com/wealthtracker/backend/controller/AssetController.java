package com.wealthtracker.backend.controller;

import com.wealthtracker.backend.dto.asset.AssetRequest;
import com.wealthtracker.backend.dto.asset.AssetResponse;
import com.wealthtracker.backend.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public List<AssetResponse> getAllAssets() {
        return assetService.getAllAssets();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetResponse createAsset(@Valid @RequestBody AssetRequest request) {
        return assetService.createAsset(request);
    }

    @PutMapping("/{assetId}")
    public AssetResponse updateAsset(@PathVariable Long assetId, @Valid @RequestBody AssetRequest request) {
        return assetService.updateAsset(assetId, request);
    }

    @DeleteMapping("/{assetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsset(@PathVariable Long assetId) {
        assetService.deleteAsset(assetId);
    }
}
