package com.claytonbc.barbershop.dto;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        Long clientId,
        Long barberId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}