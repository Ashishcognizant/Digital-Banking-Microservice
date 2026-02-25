package com.cts.analytics.service;

import com.cts.analytics.client.TransactionClient;
import com.cts.analytics.dto.FinancialReportRequest;
import com.cts.analytics.dto.TransactionDTO;
import com.cts.analytics.dto.TransactionTrendPoint;
import com.cts.analytics.model.FinancialReport;
import com.cts.analytics.repository.FinancialReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialAnalyticsService {

    private final FinancialReportRepository financialReportRepository;
    private final TransactionClient transactionClient;

    private static final double DEFAULT_FRAUD_THRESHOLD = 100000.0;

    @Transactional
    public FinancialReport generateCompliance(FinancialReportRequest req) {
        LocalDateTime from = req.getFrom();
        LocalDateTime to = req.getTo();
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' must be <= 'to'");
        }

        double threshold = (req.getFraudAmountThreshold() != null)
                ? req.getFraudAmountThreshold()
                : DEFAULT_FRAUD_THRESHOLD;

        List<TransactionDTO> txns = transactionClient.getByDateRange(from, to);

        long totalTransactions = txns.size();
        double totalAmount = txns.stream()
                .map(TransactionDTO::getAmount)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        long fraudAlerts = txns.stream()
                .filter(t -> t.getAmount() != null && t.getAmount() >= threshold)
                .count();

        FinancialReport report = FinancialReport.builder()
                .totalTransactions(totalTransactions)
                .totalAmount(totalAmount)
                .fraudAlerts(fraudAlerts)
                .generatedDate(LocalDateTime.now())
                .periodStart(from)
                .periodEnd(to)
                .build();

        return financialReportRepository.save(report);
    }

    public List<FinancialReport> listReports() {
        return financialReportRepository.findAll();
    }

    public FinancialReport getReport(Long id) {
        return financialReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));
    }

    public List<TransactionTrendPoint> getTrends(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' must be <= 'to'");
        }
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        List<TransactionDTO> txns = transactionClient.getByDateRange(start, end);

        Map<LocalDate, List<TransactionDTO>> grouped = txns.stream()
                .collect(Collectors.groupingBy(t -> t.getDate().toLocalDate()));

        return grouped.entrySet().stream()
                .map(e -> {
                    LocalDate day = e.getKey();
                    List<TransactionDTO> list = e.getValue();

                    long count = list.size();
                    double totalAmount = list.stream()
                            .map(TransactionDTO::getAmount)
                            .filter(Objects::nonNull)
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    long deposits = list.stream().filter(t -> "DEPOSIT".equalsIgnoreCase(t.getType())).count();
                    long withdrawals = list.stream().filter(t -> "WITHDRAWAL".equalsIgnoreCase(t.getType())).count();
                    long transfers = list.stream().filter(t -> "TRANSFER".equalsIgnoreCase(t.getType())).count();

                    return TransactionTrendPoint.builder()
                            .date(day)
                            .count(count)
                            .totalAmount(totalAmount)
                            .deposits(deposits)
                            .withdrawals(withdrawals)
                            .transfers(transfers)
                            .build();
                })
                .sorted(Comparator.comparing(TransactionTrendPoint::getDate))
                .toList();
    }
}
