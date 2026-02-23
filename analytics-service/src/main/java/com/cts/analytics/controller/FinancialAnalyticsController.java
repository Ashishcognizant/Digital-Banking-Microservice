package com.cts.analytics.controller;

import com.cts.analytics.dto.FinancialReportRequest;
import com.cts.analytics.dto.FinancialReportResponse;
import com.cts.analytics.dto.TransactionTrendPoint;
import com.cts.analytics.model.FinancialReport;
import com.cts.analytics.service.FinancialAnalyticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class FinancialAnalyticsController {

    private final FinancialAnalyticsService analyticsService;

    @PostMapping("/reports/compliance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialReportResponse> generateCompliance(@Valid @RequestBody FinancialReportRequest req) {
        FinancialReport saved = analyticsService.generateCompliance(req);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<List<FinancialReportResponse>> listReports() {
        List<FinancialReportResponse> list = analyticsService.listReports().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/reports/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<FinancialReportResponse> getById(@PathVariable Long id) {
        FinancialReport rep = analyticsService.getReport(id);
        return ResponseEntity.ok(toResponse(rep));
    }

    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<List<TransactionTrendPoint>> trends(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(analyticsService.getTrends(from, to));
    }

    private FinancialReportResponse toResponse(FinancialReport r) {
        return FinancialReportResponse.builder()
                .reportId(r.getReportId())
                .totalTransactions(r.getTotalTransactions())
                .totalAmount(r.getTotalAmount())
                .fraudAlerts(r.getFraudAlerts())
                .period(r.getPeriodStart() + " → " + r.getPeriodEnd())
                .generatedAt(String.valueOf(r.getGeneratedDate()))
                .build();
    }
}
