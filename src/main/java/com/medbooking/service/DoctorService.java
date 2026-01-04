package com.medbooking.service;

import com.medbooking.dto.DoctorResponseDto;
import com.medbooking.model.Doctor;
import com.medbooking.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<DoctorResponseDto> getDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors;

        if (specialization == null || specialization.isEmpty()) {
            doctors = doctorRepository.findAll();
        } else {
            doctors = doctorRepository.findAllBySpecialization(specialization);
        }

        return doctors.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Використання патерну Builder
    private DoctorResponseDto mapToDto(Doctor doctor) {
        return DoctorResponseDto.builder()
                .doctorId(doctor.getId())
                // ВИПРАВЛЕНО: беремо повне ім'я, як воно записано в User
                .fullName(doctor.getUser().getFullName())
                .email(doctor.getUser().getEmail())
                .specialization(doctor.getSpecialization())
                .description(doctor.getDescription())
                .build();
    }

    // Метод для адмінки: Отримати всіх лікарів (без фільтрів)
    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDto) // Використовуємо існуючий метод мапінгу
                .collect(Collectors.toList());
    }
}