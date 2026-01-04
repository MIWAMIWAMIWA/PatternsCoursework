package com.medbooking.service.report;

import com.medbooking.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppointmentReportStrategy implements ReportStrategy {

    private final AppointmentRepository appointmentRepository;

    @Override
    public String getReportType() {
        return "APPOINTMENTS";
    }

    @Override
    public Map<String, Long> generateReport(LocalDateTime start, LocalDateTime end, Long doctorId) {
        Map<String, Long> result = new HashMap<>();

        if (doctorId != null) {
            // Якщо вибрано конкретного лікаря - повертаємо одну цифру
            long count = appointmentRepository.countByDoctorIdAndDateTimeBetween(doctorId, start, end);
            result.put("Кількість прийомів (обраний лікар)", count);
        } else {
            // Якщо лікар не вибраний - показуємо статистику по всіх лікарях (хто скільки прийняв)
            List<Object[]> stats = appointmentRepository.countAppointmentsPerDoctorInPeriod(start, end);
            for (Object[] row : stats) {
                String doctorName = (String) row[0];
                Long count = (Long) row[1];
                result.put(doctorName, count);
            }
        }
        return result;
    }
}