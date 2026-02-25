package com.cts.analytics.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private long totalTransactions;

    private Double totalAmount;

    private long fraudAlerts;

    private LocalDateTime periodStart;

    private LocalDateTime periodEnd;

    private LocalDateTime generatedDate;
}
