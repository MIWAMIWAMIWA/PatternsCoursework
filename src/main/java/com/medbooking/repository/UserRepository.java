package com.medbooking.repository;

import com.medbooking.model.Role;
import com.medbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Рахуємо нових пацієнтів за період
    long countByRoleAndCreatedAtBetween(Role role, LocalDateTime start, LocalDateTime end);

    // (Інші методи залишаються)
    boolean existsByEmail(String email);
    long countByRole(Role role);
}