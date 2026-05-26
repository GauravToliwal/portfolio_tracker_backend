package com.wealthtracker.backend.service;

import com.wealthtracker.backend.dto.transaction.TransactionHistoryItemResponse;
import com.wealthtracker.backend.entity.Expense;
import com.wealthtracker.backend.entity.IncomeEntry;
import com.wealthtracker.backend.entity.SavingsEntry;
import com.wealthtracker.backend.repository.ExpenseRepository;
import com.wealthtracker.backend.repository.IncomeEntryRepository;
import com.wealthtracker.backend.repository.SavingsEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final CurrentUserService currentUserService;
    private final ExpenseRepository expenseRepository;
    private final IncomeEntryRepository incomeEntryRepository;
    private final SavingsEntryRepository savingsEntryRepository;

    public List<TransactionHistoryItemResponse> getTransactionHistory(String type, String category, LocalDate from, LocalDate to) {
        Long userId = currentUserService.getCurrentUser().getId();
        List<TransactionHistoryItemResponse> items = new ArrayList<>();

        expenseRepository.findAllByUserIdOrderByOccurredOnDesc(userId)
            .forEach(expense -> items.add(toExpenseItem(expense)));
        incomeEntryRepository.findAllByUserIdOrderByReceivedOnDesc(userId)
            .forEach(income -> items.add(toIncomeItem(income)));
        savingsEntryRepository.findAllByUserIdOrderBySavedOnDesc(userId)
            .forEach(savings -> items.add(toSavingsItem(savings)));

        return items.stream()
            .filter(item -> matchesType(item, type))
            .filter(item -> matchesCategory(item, category))
            .filter(item -> matchesDateRange(item, from, to))
            .sorted(Comparator.comparing(TransactionHistoryItemResponse::transactionDate).reversed())
            .toList();
    }

    private boolean matchesType(TransactionHistoryItemResponse item, String type) {
        return type == null || type.isBlank() || item.type().equalsIgnoreCase(type);
    }

    private boolean matchesCategory(TransactionHistoryItemResponse item, String category) {
        return category == null || category.isBlank() || Objects.equals(item.category().toLowerCase(Locale.ROOT), category.toLowerCase(Locale.ROOT));
    }

    private boolean matchesDateRange(TransactionHistoryItemResponse item, LocalDate from, LocalDate to) {
        boolean afterFrom = from == null || !item.transactionDate().isBefore(from);
        boolean beforeTo = to == null || !item.transactionDate().isAfter(to);
        return afterFrom && beforeTo;
    }

    private TransactionHistoryItemResponse toExpenseItem(Expense expense) {
        return new TransactionHistoryItemResponse(
            "expense-" + expense.getId(),
            "EXPENSE",
            expense.getTitle(),
            expense.getCategory().name(),
            expense.getAmount(),
            expense.getOccurredOn(),
            expense.getNote()
        );
    }

    private TransactionHistoryItemResponse toIncomeItem(IncomeEntry incomeEntry) {
        return new TransactionHistoryItemResponse(
            "income-" + incomeEntry.getId(),
            "INCOME",
            incomeEntry.getSource(),
            incomeEntry.getCategory().name(),
            incomeEntry.getAmount(),
            incomeEntry.getReceivedOn(),
            incomeEntry.getNote()
        );
    }

    private TransactionHistoryItemResponse toSavingsItem(SavingsEntry savingsEntry) {
        return new TransactionHistoryItemResponse(
            "savings-" + savingsEntry.getId(),
            "SAVINGS",
            savingsEntry.getGoalName(),
            savingsEntry.getCategory().name(),
            savingsEntry.getAmount(),
            savingsEntry.getSavedOn(),
            savingsEntry.getNote()
        );
    }
}
