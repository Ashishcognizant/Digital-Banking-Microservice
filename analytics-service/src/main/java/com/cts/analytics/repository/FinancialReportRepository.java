package com.cts.analytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.analytics.model.FinancialReport;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long> {
}
