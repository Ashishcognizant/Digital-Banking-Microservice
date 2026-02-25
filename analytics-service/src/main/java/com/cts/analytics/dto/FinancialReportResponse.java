package com.cts.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialReportResponse {
    private Long reportId;
    private long totalTransactions;
    private Double totalAmount;
    private long fraudAlerts;
    private String period;
    private String generatedAt;
}
