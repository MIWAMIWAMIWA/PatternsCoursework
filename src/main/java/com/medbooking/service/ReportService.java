package com.medbooking.service;

import com.medbooking.service.report.ReportStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final Map<String, ReportStrategy> strategies;

    // Spring автоматично знайде всі класи, що імплементують ReportStrategy, і покладе їх у Map
    public ReportService(List<ReportStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ReportStrategy::getReportType, Function.identity()));
    }

    public Map<String, Long> getReportData(String type, LocalDate startDate, LocalDate endDate, Long doctorId) {
        ReportStrategy strategy = strategies.get(type.toUpperCase());

        if (strategy == null) {
            throw new IllegalArgumentException("Невідомий тип звіту: " + type);
        }

        // Перетворюємо дати в LocalDateTime (початок дня і кінець дня)
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return strategy.generateReport(start, end, doctorId);
    }
}