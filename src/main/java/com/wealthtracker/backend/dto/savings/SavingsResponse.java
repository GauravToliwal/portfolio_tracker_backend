package com.wealthtracker.backend.dto.savings;

import com.wealthtracker.backend.enums.SavingsCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingsResponse(
    Long id,
    String goalName,
    String note,
    BigDecimal amount,
    SavingsCategory category,
    LocalDate savedOn
) {
}
