package com.claytonbc.barbershop.dto;

import com.claytonbc.barbershop.enums.Status;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long clientId,
        Long barberId,
        Status status
) {}