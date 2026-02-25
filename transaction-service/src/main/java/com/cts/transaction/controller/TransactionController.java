package com.cts.transaction.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cts.transaction.dto.TransactionRequest;
import com.cts.transaction.dto.TransactionResponse;
import com.cts.transaction.model.Transaction;
import com.cts.transaction.repository.TransactionRepository;
import com.cts.transaction.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService,
            TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest request) {
        TransactionResponse resp = transactionService.deposit(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request) {
        TransactionResponse resp = transactionService.withdraw(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransactionRequest request) {
        TransactionResponse resp = transactionService.transfer(request);
        return ResponseEntity.ok(resp);
    }

    /** List all transactions. */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<List<Transaction>> listAll() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    /**
     * Get transactions by date range (used by analytics-service via FeignClient).
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<List<Transaction>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(transactionRepository.findByDateBetween(from, to));
    }
}
