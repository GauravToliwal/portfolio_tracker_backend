package com.wealthtracker.backend.controller;

import com.wealthtracker.backend.dto.income.IncomeRequest;
import com.wealthtracker.backend.dto.income.IncomeResponse;
import com.wealthtracker.backend.service.IncomeService;
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
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @GetMapping
    public List<IncomeResponse> getAllIncomes() {
        return incomeService.getAllIncomes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncomeResponse createIncome(@Valid @RequestBody IncomeRequest request) {
        return incomeService.createIncome(request);
    }

    @PutMapping("/{incomeId}")
    public IncomeResponse updateIncome(@PathVariable Long incomeId, @Valid @RequestBody IncomeRequest request) {
        return incomeService.updateIncome(incomeId, request);
    }

    @DeleteMapping("/{incomeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIncome(@PathVariable Long incomeId) {
        incomeService.deleteIncome(incomeId);
    }
}
