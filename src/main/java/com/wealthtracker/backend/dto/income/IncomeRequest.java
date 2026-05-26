package com.wealthtracker.backend.dto.income;

import com.wealthtracker.backend.enums.IncomeCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeRequest(
    @NotBlank String source,
    String note,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount,
    @NotNull IncomeCategory category,
    @NotNull LocalDate receivedOn
) {
}
