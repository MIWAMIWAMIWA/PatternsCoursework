package com.medbooking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DoctorAppointmentDto {
    private Long appointmentId;
    private String patientName;
    private LocalDateTime dateTime;
    private String status;
    private String patientPhone; // Корисно для лікаря
}