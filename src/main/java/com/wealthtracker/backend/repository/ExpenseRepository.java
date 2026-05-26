package com.wealthtracker.backend.repository;

import com.wealthtracker.backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByUserIdOrderByOccurredOnDesc(Long userId);
}
