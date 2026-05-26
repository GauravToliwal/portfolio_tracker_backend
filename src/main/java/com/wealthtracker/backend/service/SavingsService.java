package com.wealthtracker.backend.service;

import com.wealthtracker.backend.dto.savings.SavingsRequest;
import com.wealthtracker.backend.dto.savings.SavingsResponse;
import com.wealthtracker.backend.entity.SavingsEntry;
import com.wealthtracker.backend.exception.ResourceNotFoundException;
import com.wealthtracker.backend.repository.SavingsEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingsService {

    private final SavingsEntryRepository savingsEntryRepository;
    private final CurrentUserService currentUserService;

    public List<SavingsResponse> getAllSavings() {
        Long userId = currentUserService.getCurrentUser().getId();
        return savingsEntryRepository.findAllByUserIdOrderBySavedOnDesc(userId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public SavingsResponse createSavings(SavingsRequest request) {
        SavingsEntry savingsEntry = new SavingsEntry();
        apply(savingsEntry, request);
        savingsEntry.setUser(currentUserService.getCurrentUser());
        return toResponse(savingsEntryRepository.save(savingsEntry));
    }

    public SavingsResponse updateSavings(Long savingsId, SavingsRequest request) {
        SavingsEntry savingsEntry = findOwnedSavings(savingsId);
        apply(savingsEntry, request);
        return toResponse(savingsEntryRepository.save(savingsEntry));
    }

    public void deleteSavings(Long savingsId) {
        savingsEntryRepository.delete(findOwnedSavings(savingsId));
    }

    private SavingsEntry findOwnedSavings(Long savingsId) {
        Long currentUserId = currentUserService.getCurrentUser().getId();
        SavingsEntry savingsEntry = savingsEntryRepository.findById(savingsId)
            .orElseThrow(() -> new ResourceNotFoundException("Savings record not found"));
        if (!savingsEntry.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Savings record not found");
        }
        return savingsEntry;
    }

    private void apply(SavingsEntry savingsEntry, SavingsRequest request) {
        savingsEntry.setGoalName(request.goalName());
        savingsEntry.setNote(request.note());
        savingsEntry.setAmount(request.amount());
        savingsEntry.setCategory(request.category());
        savingsEntry.setSavedOn(request.savedOn());
    }

    private SavingsResponse toResponse(SavingsEntry savingsEntry) {
        return new SavingsResponse(
            savingsEntry.getId(),
            savingsEntry.getGoalName(),
            savingsEntry.getNote(),
            savingsEntry.getAmount(),
            savingsEntry.getCategory(),
            savingsEntry.getSavedOn()
        );
    }
}
