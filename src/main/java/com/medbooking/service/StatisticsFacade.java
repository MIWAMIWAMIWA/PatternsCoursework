package com.medbooking.service;

import com.medbooking.dto.AdminStatsDto;
import com.medbooking.model.Doctor;
import com.medbooking.model.Role;
import com.medbooking.repository.AppointmentRepository;
import com.medbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // У Spring Service часто виконує роль Фасаду бізнес-логіки
@RequiredArgsConstructor
public class StatisticsFacade {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    // Єдиний публічний метод, який викликає контролер
    public AdminStatsDto getFullStatistics() {
        return AdminStatsDto.builder()
                .appointmentsToday(countAppointmentsToday())
                .appointmentsThisMonth(countAppointmentsThisMonth())
                .totalPatients(userRepository.countByRole(Role.PATIENT))
                .doctorLoadStats(getDoctorLoad())
                .build();
    }

    // --- Приватні методи (прихована логіка) ---

    private long countAppointmentsToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return appointmentRepository.countByDateTimeBetween(startOfDay, endOfDay);
    }

    private long countAppointmentsThisMonth() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(LocalTime.MAX);
        return appointmentRepository.countByDateTimeBetween(startOfMonth, endOfMonth);
    }

    private Map<String, Long> getDoctorLoad() {
        List<Object[]> results = appointmentRepository.countAppointmentsPerDoctor();
        Map<String, Long> stats = new HashMap<>();

        for (Object[] result : results) {
            Doctor doctor = (Doctor) result[0];
            Long count = (Long) result[1];
            // Беремо ПІБ лікаря для ключа мапи
            String doctorName = doctor.getUser().getFullName();
            stats.put(doctorName, count);
        }
        return stats;
    }
}