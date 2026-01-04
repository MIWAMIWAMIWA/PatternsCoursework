package com.medbooking.controller;

import com.medbooking.dto.AppointmentRequestDto;
import com.medbooking.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentRequestDto dto) {
        try {
            appointmentService.createAppointment(dto);
            return ResponseEntity.ok("Запис успішно створено!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Помилка бронювання: " + e.getMessage());
        }
    }
}