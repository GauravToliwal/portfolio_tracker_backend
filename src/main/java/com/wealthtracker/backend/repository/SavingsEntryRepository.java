package com.wealthtracker.backend.repository;

import com.wealthtracker.backend.entity.SavingsEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsEntryRepository extends JpaRepository<SavingsEntry, Long> {

    List<SavingsEntry> findAllByUserIdOrderBySavedOnDesc(Long userId);
}
