package com.wealthtracker.backend.service;

import com.wealthtracker.backend.dto.expense.ExpenseRequest;
import com.wealthtracker.backend.dto.expense.ExpenseResponse;
import com.wealthtracker.backend.entity.Expense;
import com.wealthtracker.backend.exception.ResourceNotFoundException;
import com.wealthtracker.backend.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CurrentUserService currentUserService;

    public List<ExpenseResponse> getAllExpenses() {
        Long userId = currentUserService.getCurrentUser().getId();
        return expenseRepository.findAllByUserIdOrderByOccurredOnDesc(userId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public ExpenseResponse createExpense(ExpenseRequest request) {
        Expense expense = new Expense();
        apply(expense, request);
        expense.setUser(currentUserService.getCurrentUser());
        return toResponse(expenseRepository.save(expense));
    }

    public ExpenseResponse updateExpense(Long expenseId, ExpenseRequest request) {
        Expense expense = findOwnedExpense(expenseId);
        apply(expense, request);
        return toResponse(expenseRepository.save(expense));
    }

    public void deleteExpense(Long expenseId) {
        expenseRepository.delete(findOwnedExpense(expenseId));
    }

    private Expense findOwnedExpense(Long expenseId) {
        Long currentUserId = currentUserService.getCurrentUser().getId();
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        if (!expense.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Expense not found");
        }
        return expense;
    }

    private void apply(Expense expense, ExpenseRequest request) {
        expense.setTitle(request.title());
        expense.setNote(request.note());
        expense.setAmount(request.amount());
        expense.setCategory(request.category());
        expense.setOccurredOn(request.occurredOn());
    }

    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
            expense.getId(),
            expense.getTitle(),
            expense.getNote(),
            expense.getAmount(),
            expense.getCategory(),
            expense.getOccurredOn()
        );
    }
}
