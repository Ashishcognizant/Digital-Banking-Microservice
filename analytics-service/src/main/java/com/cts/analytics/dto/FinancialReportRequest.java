package com.cts.analytics.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FinancialReportRequest {
    private LocalDate periodStart;
    private LocalDate periodEnd;
}
