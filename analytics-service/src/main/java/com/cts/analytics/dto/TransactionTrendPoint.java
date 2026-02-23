package com.cts.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTrendPoint {
    private LocalDate date;
    private Integer transactionCount;
    private Double totalVolume;
}
