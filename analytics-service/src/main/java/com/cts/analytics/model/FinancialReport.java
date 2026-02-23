package com.cts.analytics.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    private Integer totalTransactions;
    
    private Double totalAmount;
    
    private Integer fraudAlerts;

    private LocalDate periodStart;
    
    private LocalDate periodEnd;

    private LocalDate generatedDate;
}
