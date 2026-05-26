package com.wealthtracker.backend.dto.dashboard;

import java.math.BigDecimal;

public record DashboardSummaryResponse(
    BigDecimal totalBalance,
    BigDecimal monthlyExpenses,
    BigDecimal monthlyIncome,
    BigDecimal totalSavings,
    BigDecimal netWorth
) {
}
