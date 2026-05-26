package com.wealthtracker.backend.service;

import com.wealthtracker.backend.dto.income.IncomeRequest;
import com.wealthtracker.backend.dto.income.IncomeResponse;
import com.wealthtracker.backend.entity.IncomeEntry;
import com.wealthtracker.backend.exception.ResourceNotFoundException;
import com.wealthtracker.backend.repository.IncomeEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeEntryRepository incomeEntryRepository;
    private final CurrentUserService currentUserService;

    public List<IncomeResponse> getAllIncomes() {
        Long userId = currentUserService.getCurrentUser().getId();
        return incomeEntryRepository.findAllByUserIdOrderByReceivedOnDesc(userId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public IncomeResponse createIncome(IncomeRequest request) {
        IncomeEntry incomeEntry = new IncomeEntry();
        apply(incomeEntry, request);
        incomeEntry.setUser(currentUserService.getCurrentUser());
        return toResponse(incomeEntryRepository.save(incomeEntry));
    }

    public IncomeResponse updateIncome(Long incomeId, IncomeRequest request) {
        IncomeEntry incomeEntry = findOwnedIncome(incomeId);
        apply(incomeEntry, request);
        return toResponse(incomeEntryRepository.save(incomeEntry));
    }

    public void deleteIncome(Long incomeId) {
        incomeEntryRepository.delete(findOwnedIncome(incomeId));
    }

    private IncomeEntry findOwnedIncome(Long incomeId) {
        Long currentUserId = currentUserService.getCurrentUser().getId();
        IncomeEntry incomeEntry = incomeEntryRepository.findById(incomeId)
            .orElseThrow(() -> new ResourceNotFoundException("Income record not found"));
        if (!incomeEntry.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Income record not found");
        }
        return incomeEntry;
    }

    private void apply(IncomeEntry incomeEntry, IncomeRequest request) {
        incomeEntry.setSource(request.source());
        incomeEntry.setNote(request.note());
        incomeEntry.setAmount(request.amount());
        incomeEntry.setCategory(request.category());
        incomeEntry.setReceivedOn(request.receivedOn());
    }

    private IncomeResponse toResponse(IncomeEntry incomeEntry) {
        return new IncomeResponse(
            incomeEntry.getId(),
            incomeEntry.getSource(),
            incomeEntry.getNote(),
            incomeEntry.getAmount(),
            incomeEntry.getCategory(),
            incomeEntry.getReceivedOn()
        );
    }
}
