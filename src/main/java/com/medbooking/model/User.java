package com.medbooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    // --- НОВЕ ПОЛЕ ---
    @Column(updatable = false) // Дату реєстрації змінювати не можна
    private LocalDateTime createdAt;

    // Цей метод запуститься автоматично перед INSERT в базу
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}