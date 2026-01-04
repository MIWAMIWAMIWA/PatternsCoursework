package com.medbooking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder   // Реалізація патерну "Будівельник" (GoF)
public class DoctorResponseDto {
    private Long doctorId;
    private String fullName;
    private String specialization;
    private String email;
    private String description;
}