package com.cts.analytics.service;

import com.cts.analytics.dto.FinancialReportRequest;
import com.cts.analytics.dto.TransactionTrendPoint;
import com.cts.analytics.model.FinancialReport;
import com.cts.analytics.repository.FinancialReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialAnalyticsService {

    private final FinancialReportRepository reportRepository;

    public FinancialReport generateCompliance(FinancialReportRequest req) {
        // Mock Implementation for microservices example
        FinancialReport report = FinancialReport.builder()
                .totalTransactions(100)
                .totalAmount(50000.0)
                .fraudAlerts(2)
                .periodStart(req.getPeriodStart())
                .periodEnd(req.getPeriodEnd())
                .generatedDate(LocalDate.now())
                .build();
        return reportRepository.save(report);
    }

    public List<FinancialReport> listReports() {
        return reportRepository.findAll();
    }

    public FinancialReport getReport(Long id) {
        return reportRepository.findById(id).orElseThrow();
    }

    public List<TransactionTrendPoint> getTrends(LocalDate from, LocalDate to) {
        // Mock Implementation
        return List.of(
                new TransactionTrendPoint(from, 10, 5000.0),
                new TransactionTrendPoint(to, 15, 7500.0)
        );
    }
}
