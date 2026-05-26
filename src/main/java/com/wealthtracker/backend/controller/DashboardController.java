package com.wealthtracker.backend.controller;

import com.wealthtracker.backend.dto.dashboard.AssetAllocationItem;
import com.wealthtracker.backend.dto.dashboard.DashboardSummaryResponse;
import com.wealthtracker.backend.dto.dashboard.ExpenseCategoryBreakdownItem;
import com.wealthtracker.backend.dto.dashboard.IncomeExpenseTrendItem;
import com.wealthtracker.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/expense-categories")
    public List<ExpenseCategoryBreakdownItem> getExpenseCategoryBreakdown() {
        return dashboardService.getExpenseCategoryBreakdown();
    }

    @GetMapping("/income-vs-expenses")
    public List<IncomeExpenseTrendItem> getIncomeVsExpensesTrend() {
        return dashboardService.getIncomeVsExpensesTrend();
    }

    @GetMapping("/asset-allocation")
    public List<AssetAllocationItem> getAssetAllocation() {
        return dashboardService.getAssetAllocation();
    }
}
