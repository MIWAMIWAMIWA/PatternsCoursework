package com.medbooking.controller;

import com.medbooking.dto.AdminStatsDto;
import com.medbooking.dto.AppointmentAdminDto;
import com.medbooking.dto.DoctorResponseDto;
import com.medbooking.dto.PatientResponseDto;
import com.medbooking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    // Вже існуючі сервіси для звітів
    private final StatisticsFacade statisticsFacade;
    private final ReportService reportService;

    // Нові сервіси для керування списками
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService; // Якщо його не було - додайте в імпорт і сюди

    // --- ЗВІТИ (ВЖЕ БУЛИ) ---

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getDashboardStats() {
        return ResponseEntity.ok(statisticsFacade.getFullStatistics());
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Long>> getCustomReport(
            @RequestParam String type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) Long doctorId
    ) {
        return ResponseEntity.ok(reportService.getReportData(type, start, end, doctorId));
    }

    // --- НОВІ МЕТОДИ ДЛЯ КЕРУВАННЯ (СПИСКИ) ---

    // 1. Отримати всіх лікарів
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // 2. Отримати всіх пацієнтів
    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // 3. Отримати всі записи (глобальний розклад)
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentAdminDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
}