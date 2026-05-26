package com.wealthtracker.backend.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionHistoryItemResponse(
    String id,
    String type,
    String title,
    String category,
    BigDecimal amount,
    LocalDate transactionDate,
    String note
) {
}
