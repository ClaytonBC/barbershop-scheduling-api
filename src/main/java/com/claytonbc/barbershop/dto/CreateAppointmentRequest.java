package com.claytonbc.barbershop.dto;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        Long barberId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}