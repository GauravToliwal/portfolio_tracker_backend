package com.wealthtracker.backend.controller;

import com.wealthtracker.backend.dto.transaction.TransactionHistoryItemResponse;
import com.wealthtracker.backend.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @GetMapping("/history")
    public List<TransactionHistoryItemResponse> getTransactionHistory(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return transactionHistoryService.getTransactionHistory(type, category, from, to);
    }
}
