package com.medbooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime; // Дата і час візиту

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    // Зв'язок з пацієнтом (User)
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    // Зв'язок з лікарем (Doctor)
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}