package com.cts.transaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.transaction.dto.TransactionRequest;
import com.cts.transaction.dto.TransactionResponse;
import com.cts.transaction.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
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
}
