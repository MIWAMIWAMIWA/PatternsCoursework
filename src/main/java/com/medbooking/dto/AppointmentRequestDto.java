package com.medbooking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDto {
    private Long doctorId;
    private Long patientId; // У реальній системі це береться з токена, але для курсової можна так
    private LocalDateTime dateTime; // Формат JSON: "2025-01-20T14:30:00"
}