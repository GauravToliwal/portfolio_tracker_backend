package com.wealthtracker.backend.repository;

import com.wealthtracker.backend.entity.IncomeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeEntryRepository extends JpaRepository<IncomeEntry, Long> {

    List<IncomeEntry> findAllByUserIdOrderByReceivedOnDesc(Long userId);
}
