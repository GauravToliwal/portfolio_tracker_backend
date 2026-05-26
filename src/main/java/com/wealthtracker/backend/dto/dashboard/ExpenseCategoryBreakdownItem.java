package com.wealthtracker.backend.dto.dashboard;

import java.math.BigDecimal;

public record ExpenseCategoryBreakdownItem(
    String label,
    BigDecimal value
) {
}
