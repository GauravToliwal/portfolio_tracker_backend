package com.wealthtracker.backend.dto.asset;

import com.wealthtracker.backend.enums.AssetType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetRequest(
    @NotBlank String name,
    String institution,
    @NotNull AssetType assetType,
    @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal quantity,
    @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal investedValue,
    @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal currentValue,
    @NotNull LocalDate valuationDate
) {
}
