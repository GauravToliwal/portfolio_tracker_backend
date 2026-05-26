package com.wealthtracker.backend.dto.expense;

import com.wealthtracker.backend.enums.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(
    Long id,
    String title,
    String note,
    BigDecimal amount,
    ExpenseCategory category,
    LocalDate occurredOn
) {
}
