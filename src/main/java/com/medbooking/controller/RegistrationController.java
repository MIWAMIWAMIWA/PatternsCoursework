package com.medbooking.controller;

import com.medbooking.dto.PatientRegistrationDto;
import com.medbooking.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor // Генерує конструктор для patientService
public class RegistrationController {

    private final PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<String> registerPatient(@RequestBody PatientRegistrationDto registrationDto) {
        try {
            patientService.registerPatient(registrationDto);
            return ResponseEntity.ok("Реєстрація успішна! Тепер ви можете увійти.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Помилка: " + e.getMessage());
        }
    }
}