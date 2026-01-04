package com.medbooking.service.template;

import com.medbooking.dto.DoctorAppointmentDto;
import com.medbooking.model.Appointment;
import com.medbooking.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractAppointmentProcessor {

    protected final DoctorRepository doctorRepository;

    // --- TEMPLATE METHOD (Шаблонний метод) ---
    // Це "скелет" алгоритму. Він final, щоб його не можна було змінити.
    public final List<DoctorAppointmentDto> getAppointments(Long doctorId) {
        // Крок 1: Валідація (Спільна логіка)
        validateDoctor(doctorId);

        // Крок 2: Отримання даних (Абстрактний метод - реалізується в підкласах)
        List<Appointment> appointments = fetchAppointments(doctorId);

        // Крок 3: Конвертація в DTO (Спільна логіка)
        return mapToDto(appointments);
    }
    // Спільний метод
    private void validateDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Лікаря з ID " + doctorId + " не знайдено");
        }
    }
    // Спільний метод мапінгу
    private List<DoctorAppointmentDto> mapToDto(List<Appointment> appointments) {
        return appointments.stream().map(app -> {
            DoctorAppointmentDto dto = new DoctorAppointmentDto();
            dto.setAppointmentId(app.getId());
            dto.setDateTime(app.getDateTime());
            dto.setStatus(app.getStatus().name());
            dto.setPatientName(app.getPatient().getFullName()); // Беремо з User
            dto.setPatientPhone(app.getPatient().getPhone());
            return dto;
        }).collect(Collectors.toList());
    }
    // --- АБСТРАКТНИЙ МЕТОД (Hook) ---
    // Кожен нащадок мусить реалізувати свою логіку вибірки даних
    protected abstract List<Appointment> fetchAppointments(Long doctorId);
}