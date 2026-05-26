package com.wealthtracker.backend.dto.expense;

import com.wealthtracker.backend.enums.ExpenseCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
    @NotBlank String title,
    String note,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount,
    @NotNull ExpenseCategory category,
    @NotNull LocalDate occurredOn
) {
}
