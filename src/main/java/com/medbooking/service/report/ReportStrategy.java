package com.medbooking.service.report;

import java.time.LocalDateTime;
import java.util.Map;

public interface ReportStrategy {
    // Метод повертає дані для графіка (Key -> Value)
    Map<String, Long> generateReport(LocalDateTime start, LocalDateTime end, Long doctorId);

    // Щоб фабрика знала, яку стратегію вибрати ("appointments", "patients" тощо)
    String getReportType();
}