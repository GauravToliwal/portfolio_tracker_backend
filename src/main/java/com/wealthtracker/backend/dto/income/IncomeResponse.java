package com.wealthtracker.backend.dto.income;

import com.wealthtracker.backend.enums.IncomeCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeResponse(
    Long id,
    String source,
    String note,
    BigDecimal amount,
    IncomeCategory category,
    LocalDate receivedOn
) {
}
