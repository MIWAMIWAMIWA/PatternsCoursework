package com.medbooking.service.template;

import com.medbooking.model.Appointment;
import com.medbooking.repository.AppointmentRepository;
import com.medbooking.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryAppointmentProcessor extends AbstractAppointmentProcessor {

    private final AppointmentRepository appointmentRepository;

    public HistoryAppointmentProcessor(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        super(doctorRepository);
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    protected List<Appointment> fetchAppointments(Long doctorId) {
        // Логіка для історії: беремо тільки минулі записи
        return appointmentRepository.findAllByDoctorIdAndDateTimeBeforeOrderByDateTimeDesc(doctorId, LocalDateTime.now());
    }
}