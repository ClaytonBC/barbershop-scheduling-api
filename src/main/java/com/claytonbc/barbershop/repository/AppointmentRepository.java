package com.claytonbc.barbershop.repository;

import com.claytonbc.barbershop.entity.Appointment;
import com.claytonbc.barbershop.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByBarberIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
            Long barberId,
            Status status,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    List<Appointment> findByClientId(Long clientId);

    List<Appointment> findByBarberId(Long barberId);
}
