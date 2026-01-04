package com.medbooking.repository;

import com.medbooking.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // --- 1. ВАЛІДАЦІЯ (Запис на прийом) ---
    boolean existsByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);

    // --- 2. ДЛЯ ЗВІТІВ (Facade & Admin Dashboard) ---
    long countByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a.doctor, COUNT(a) FROM Appointment a GROUP BY a.doctor")
    List<Object[]> countAppointmentsPerDoctor();

    // --- 3. ДЛЯ СТРАТЕГІЇ (Детальні звіти) ---
    long countByDoctorIdAndDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a.doctor.user.fullName, COUNT(a) FROM Appointment a " +
            "WHERE a.dateTime BETWEEN :start AND :end " +
            "GROUP BY a.doctor.user.fullName")
    List<Object[]> countAppointmentsPerDoctorInPeriod(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);

    // --- 4. ДЛЯ КАБІНЕТУ ЛІКАРЯ (Template Method - НОВЕ) ---

    // Майбутні прийоми (Розклад) - сортуємо від найближчого
    List<Appointment> findAllByDoctorIdAndDateTimeAfterOrderByDateTimeAsc(Long doctorId, LocalDateTime now);

    // Минулі прийоми (Історія) - сортуємо від нових до старих
    List<Appointment> findAllByDoctorIdAndDateTimeBeforeOrderByDateTimeDesc(Long doctorId, LocalDateTime now);
}