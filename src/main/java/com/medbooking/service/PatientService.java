package com.medbooking.service;

import com.medbooking.dto.PatientRegistrationDto;
import com.medbooking.dto.PatientResponseDto;
import com.medbooking.model.User;
import com.medbooking.model.Role;
import com.medbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Генерує конструктор для userRepository автоматично
public class PatientService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;

    public void registerPatient(PatientRegistrationDto dto) {
        // 1. Перевірка бізнес-правил
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Користувач з таким email вже існує!");
        }

        // 2. Використання патерну BUILDER (Lombok реалізація)
        User newPatient = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .password(dto.getPassword()) // Поки що без хешування, як у твоєму коді
                .role(Role.PATIENT)
                .build();

        // 3. Збереження в базу
        userRepository.save(newPatient);

        System.out.println("Пацієнт " + newPatient.getEmail() + " успішно зареєстрований.");
    }

    // Метод для адмінки: Отримати всіх пацієнтів
    public List<PatientResponseDto> getAllPatients() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.PATIENT) // Беремо тільки пацієнтів
                .map(u -> PatientResponseDto.builder()
                        .id(u.getId())
                        .fullName(u.getFullName())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .dateOfBirth(u.getDateOfBirth())
                        .build())
                .collect(Collectors.toList());
    }
}