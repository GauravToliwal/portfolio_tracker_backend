package com.wealthtracker.backend.dto.dashboard;

import java.math.BigDecimal;

public record AssetAllocationItem(
    String label,
    BigDecimal value
) {
}
