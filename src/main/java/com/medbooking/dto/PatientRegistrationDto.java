package com.medbooking.dto;

import lombok.Data;
import java.time.LocalDate;

@Data // Автоматично створює геттери, сеттери, toString
public class PatientRegistrationDto {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private LocalDate dateOfBirth;
}