package com.cts.transaction.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private Long accountId;
    private Long toAccountId;
    private Double amount;
}
