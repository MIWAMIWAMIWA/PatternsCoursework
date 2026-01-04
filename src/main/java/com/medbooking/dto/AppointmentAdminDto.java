package com.medbooking.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentAdminDto {
    private Long id;
    private LocalDateTime dateTime;
    private String status;      // PLANNED, COMPLETED, CANCELLED
    private String doctorName;  // ПІБ лікаря
    private String doctorSpec;  // Спеціалізація
    private String patientName; // ПІБ пацієнта
    private String patientPhone;// Телефон для зв'язку
}