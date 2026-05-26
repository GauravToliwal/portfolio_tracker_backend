package com.wealthtracker.backend.dto.savings;

import com.wealthtracker.backend.enums.SavingsCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingsRequest(
    @NotBlank String goalName,
    String note,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount,
    @NotNull SavingsCategory category,
    @NotNull LocalDate savedOn
) {
}
