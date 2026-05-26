package com.wealthtracker.backend.controller;

import com.wealthtracker.backend.dto.savings.SavingsRequest;
import com.wealthtracker.backend.dto.savings.SavingsResponse;
import com.wealthtracker.backend.service.SavingsService;
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
@RequestMapping("/api/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;

    @GetMapping
    public List<SavingsResponse> getAllSavings() {
        return savingsService.getAllSavings();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsResponse createSavings(@Valid @RequestBody SavingsRequest request) {
        return savingsService.createSavings(request);
    }

    @PutMapping("/{savingsId}")
    public SavingsResponse updateSavings(@PathVariable Long savingsId, @Valid @RequestBody SavingsRequest request) {
        return savingsService.updateSavings(savingsId, request);
    }

    @DeleteMapping("/{savingsId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSavings(@PathVariable Long savingsId) {
        savingsService.deleteSavings(savingsId);
    }
}
