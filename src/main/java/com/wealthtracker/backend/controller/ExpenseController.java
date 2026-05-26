package com.wealthtracker.backend.controller;

import com.wealthtracker.backend.dto.expense.ExpenseRequest;
import com.wealthtracker.backend.dto.expense.ExpenseResponse;
import com.wealthtracker.backend.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public List<ExpenseResponse> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse createExpense(@Valid @RequestBody ExpenseRequest request) {
        return expenseService.createExpense(request);
    }

    @PutMapping("/{expenseId}")
    public ExpenseResponse updateExpense(@PathVariable Long expenseId, @Valid @RequestBody ExpenseRequest request) {
        return expenseService.updateExpense(expenseId, request);
    }

    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
    }
}
