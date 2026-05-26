package com.wealthtracker.backend.dto.asset;

import com.wealthtracker.backend.enums.AssetType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetResponse(
    Long id,
    String name,
    String institution,
    AssetType assetType,
    BigDecimal quantity,
    BigDecimal investedValue,
    BigDecimal currentValue,
    LocalDate valuationDate
) {
}
