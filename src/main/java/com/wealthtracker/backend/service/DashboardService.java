package com.wealthtracker.backend.service;

import com.wealthtracker.backend.dto.dashboard.AssetAllocationItem;
import com.wealthtracker.backend.dto.dashboard.DashboardSummaryResponse;
import com.wealthtracker.backend.dto.dashboard.ExpenseCategoryBreakdownItem;
import com.wealthtracker.backend.dto.dashboard.IncomeExpenseTrendItem;
import com.wealthtracker.backend.entity.Asset;
import com.wealthtracker.backend.entity.Expense;
import com.wealthtracker.backend.entity.IncomeEntry;
import com.wealthtracker.backend.entity.SavingsEntry;
import com.wealthtracker.backend.enums.AssetType;
import com.wealthtracker.backend.repository.AssetRepository;
import com.wealthtracker.backend.repository.ExpenseRepository;
import com.wealthtracker.backend.repository.IncomeEntryRepository;
import com.wealthtracker.backend.repository.SavingsEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy");

    private final CurrentUserService currentUserService;
    private final ExpenseRepository expenseRepository;
    private final IncomeEntryRepository incomeEntryRepository;
    private final SavingsEntryRepository savingsEntryRepository;
    private final AssetRepository assetRepository;

    public DashboardSummaryResponse getSummary() {
        Long userId = currentUserService.getCurrentUser().getId();
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);

        List<Expense> expenses = expenseRepository.findAllByUserIdOrderByOccurredOnDesc(userId);
        List<IncomeEntry> incomes = incomeEntryRepository.findAllByUserIdOrderByReceivedOnDesc(userId);
        List<SavingsEntry> savingsEntries = savingsEntryRepository.findAllByUserIdOrderBySavedOnDesc(userId);
        List<Asset> assets = assetRepository.findAllByUserIdOrderByValuationDateDesc(userId);

        BigDecimal monthlyExpenses = expenses.stream()
            .filter(expense -> YearMonth.from(expense.getOccurredOn()).equals(currentMonth))
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthlyIncome = incomes.stream()
            .filter(income -> YearMonth.from(income.getReceivedOn()).equals(currentMonth))
            .map(IncomeEntry::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSavings = savingsEntries.stream()
            .map(SavingsEntry::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAssets = assets.stream()
            .map(Asset::getCurrentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCashAssets = assets.stream()
            .filter(asset -> asset.getAssetType() == AssetType.CASH)
            .map(Asset::getCurrentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIncome = incomes.stream()
            .map(IncomeEntry::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBalance = totalCashAssets.add(totalIncome.subtract(totalExpenses).max(BigDecimal.ZERO));
        BigDecimal netWorth = totalAssets.add(totalSavings);

        return new DashboardSummaryResponse(totalBalance, monthlyExpenses, monthlyIncome, totalSavings, netWorth);
    }

    public List<ExpenseCategoryBreakdownItem> getExpenseCategoryBreakdown() {
        Long userId = currentUserService.getCurrentUser().getId();
        Map<String, BigDecimal> totals = new java.util.LinkedHashMap<>();
        expenseRepository.findAllByUserIdOrderByOccurredOnDesc(userId).forEach(expense ->
            totals.merge(expense.getCategory().name(), expense.getAmount(), BigDecimal::add)
        );

        return totals.entrySet().stream()
            .map(entry -> new ExpenseCategoryBreakdownItem(entry.getKey(), entry.getValue()))
            .toList();
    }

    public List<IncomeExpenseTrendItem> getIncomeVsExpensesTrend() {
        Long userId = currentUserService.getCurrentUser().getId();
        List<Expense> expenses = expenseRepository.findAllByUserIdOrderByOccurredOnDesc(userId);
        List<IncomeEntry> incomes = incomeEntryRepository.findAllByUserIdOrderByReceivedOnDesc(userId);

        List<IncomeExpenseTrendItem> items = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.now().minusMonths(i);
            BigDecimal incomeTotal = incomes.stream()
                .filter(income -> YearMonth.from(income.getReceivedOn()).equals(month))
                .map(IncomeEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal expenseTotal = expenses.stream()
                .filter(expense -> YearMonth.from(expense.getOccurredOn()).equals(month))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            items.add(new IncomeExpenseTrendItem(month.format(MONTH_FORMATTER), incomeTotal, expenseTotal));
        }
        return items;
    }

    public List<AssetAllocationItem> getAssetAllocation() {
        Long userId = currentUserService.getCurrentUser().getId();
        Map<AssetType, BigDecimal> totals = new EnumMap<>(AssetType.class);
        assetRepository.findAllByUserIdOrderByValuationDateDesc(userId).forEach(asset ->
            totals.merge(asset.getAssetType(), asset.getCurrentValue(), BigDecimal::add)
        );

        return totals.entrySet().stream()
            .map(entry -> new AssetAllocationItem(entry.getKey().name(), entry.getValue()))
            .toList();
    }
}
