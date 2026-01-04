package com.medbooking.service.template;

import com.medbooking.model.Appointment;
import com.medbooking.repository.AppointmentRepository;
import com.medbooking.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UpcomingAppointmentProcessor extends AbstractAppointmentProcessor {

    private final AppointmentRepository appointmentRepository;

    public UpcomingAppointmentProcessor(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        super(doctorRepository);
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    protected List<Appointment> fetchAppointments(Long doctorId) {
        // Логіка для розкладу: беремо тільки майбутні записи
        return appointmentRepository.findAllByDoctorIdAndDateTimeAfterOrderByDateTimeAsc(doctorId, LocalDateTime.now());
    }
}