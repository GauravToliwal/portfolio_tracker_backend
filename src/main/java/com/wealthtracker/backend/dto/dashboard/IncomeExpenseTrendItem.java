package com.wealthtracker.backend.dto.dashboard;

import java.math.BigDecimal;

public record IncomeExpenseTrendItem(
    String month,
    BigDecimal income,
    BigDecimal expenses
) {
}
