package com.medbooking.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class PatientResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
}