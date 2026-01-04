package com.medbooking.service.report;

import com.medbooking.model.Role;
import com.medbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PatientReportStrategy implements ReportStrategy {

    private final UserRepository userRepository;

    @Override
    public String getReportType() {
        return "PATIENTS";
    }

    @Override
    public Map<String, Long> generateReport(LocalDateTime start, LocalDateTime end, Long doctorId) {
        Map<String, Long> result = new HashMap<>();

        // Тут логіка для динаміки. Для простоти повернемо загальну цифру за період.
        // У повноцінному дипломі тут можна розбити період по днях циклом.
        long count = userRepository.countByRoleAndCreatedAtBetween(Role.PATIENT, start, end);

        result.put("Нові пацієнти за період", count);
        return result;
    }
}