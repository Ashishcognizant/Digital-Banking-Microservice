package com.cts.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialReportResponse {
    private Long reportId;
    private Integer totalTransactions;
    private Double totalAmount;
    private Integer fraudAlerts;
    private String period;
    private String generatedAt;
}
