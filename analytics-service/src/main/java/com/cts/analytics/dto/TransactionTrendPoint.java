package com.cts.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionTrendPoint {
    private LocalDate date;
    private long count;
    private double totalAmount;
    private long deposits;
    private long withdrawals;
    private long transfers;
}
