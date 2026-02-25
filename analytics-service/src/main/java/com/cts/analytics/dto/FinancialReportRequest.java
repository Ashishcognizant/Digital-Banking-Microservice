package com.cts.analytics.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FinancialReportRequest {
    private LocalDateTime from;
    private LocalDateTime to;
    private Double fraudAmountThreshold;
}
