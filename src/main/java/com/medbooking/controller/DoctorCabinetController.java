package com.medbooking.controller;

import com.medbooking.dto.DoctorAppointmentDto;
import com.medbooking.service.template.HistoryAppointmentProcessor;
import com.medbooking.service.template.UpcomingAppointmentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-cabinet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DoctorCabinetController {

    // Впроваджуємо наші реалізації Template Method
    private final UpcomingAppointmentProcessor upcomingProcessor;
    private final HistoryAppointmentProcessor historyProcessor;

    // 1. Отримати актуальний розклад (майбутні записи)
    // GET /api/doctor-cabinet/schedule?doctorId=1
    @GetMapping("/schedule")
    public ResponseEntity<List<DoctorAppointmentDto>> getSchedule(@RequestParam Long doctorId) {
        return ResponseEntity.ok(upcomingProcessor.getAppointments(doctorId));
    }

    // 2. Отримати історію прийомів (минулі записи)
    // GET /api/doctor-cabinet/history?doctorId=1
    @GetMapping("/history")
    public ResponseEntity<List<DoctorAppointmentDto>> getHistory(@RequestParam Long doctorId) {
        return ResponseEntity.ok(historyProcessor.getAppointments(doctorId));
    }
}