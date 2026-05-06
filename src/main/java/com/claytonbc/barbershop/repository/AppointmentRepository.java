package com.claytonbc.barbershop.repository;

import com.claytonbc.barbershop.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.barber.id = :barberId
        AND a.status <> 'CANCELLED'
        AND (:start < a.endTime AND :end > a.startTime)
    """)
    List<Appointment> findConflicts(Long barberId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByClientId(Long clientId);

    List<Appointment> findByBarberId(Long barberId);
}
