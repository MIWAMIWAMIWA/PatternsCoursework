package com.medbooking.service;

import com.medbooking.dto.AppointmentAdminDto;
import com.medbooking.dto.AppointmentRequestDto;
import com.medbooking.model.Appointment;
import com.medbooking.model.AppointmentStatus;
import com.medbooking.model.Doctor;
import com.medbooking.model.User;
import com.medbooking.repository.AppointmentRepository;
import com.medbooking.repository.DoctorRepository;
import com.medbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public void createAppointment(AppointmentRequestDto dto) {
        // 1. Перевіряємо, чи існує лікар
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Лікаря не знайдено"));

        // 2. Перевіряємо, чи існує пацієнт
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Пацієнта не знайдено"));

        // 3. ВАЛІДАЦІЯ ЧАСУ: Чи вільний лікар на цей час?
        boolean isBusy = appointmentRepository.existsByDoctorIdAndDateTime(dto.getDoctorId(), dto.getDateTime());
        if (isBusy) {
            throw new RuntimeException("Цей час вже зайнятий! Оберіть інший час.");
        }

        // 4. Створення об'єкта через Builder (GoF Pattern)
        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .dateTime(dto.getDateTime())
                .status(AppointmentStatus.PLANNED) // За замовчуванням статус "Заплановано"
                .build();

        // 5. Збереження
        appointmentRepository.save(appointment);
    }

    // Метод для адмінки: Отримати абсолютно всі записи
    public List<AppointmentAdminDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                // Сортуємо: спочатку новіші записи
                .sorted((a, b) -> b.getDateTime().compareTo(a.getDateTime()))
                .map(a -> AppointmentAdminDto.builder()
                        .id(a.getId())
                        .dateTime(a.getDateTime())
                        .status(a.getStatus().name())
                        .doctorName(a.getDoctor().getUser().getFullName())
                        .doctorSpec(a.getDoctor().getSpecialization())
                        .patientName(a.getPatient().getFullName())
                        .patientPhone(a.getPatient().getPhone())
                        .build())
                .collect(Collectors.toList());
    }
}