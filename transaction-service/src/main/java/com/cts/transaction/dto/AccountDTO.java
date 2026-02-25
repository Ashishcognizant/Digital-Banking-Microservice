package com.cts.transaction.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private Long customerId;
    private String status;
    private Double balance;
}