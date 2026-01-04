package com.medbooking.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class AdminStatsDto {
    // Кількість прийомів
    private long appointmentsToday;
    private long appointmentsThisMonth;

    // Загальна кількість пацієнтів
    private long totalPatients;

    // Статистика завантаженості лікарів (Ім'я лікаря -> Кількість записів)
    // Наприклад: {"Dr. House": 5, "Dr. Strange": 10}
    private Map<String, Long> doctorLoadStats;
}