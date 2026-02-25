package com.cts.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.transaction.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDateBetween(LocalDateTime from, LocalDateTime to);
}
