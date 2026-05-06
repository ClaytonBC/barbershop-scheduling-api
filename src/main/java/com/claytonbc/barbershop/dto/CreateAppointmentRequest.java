package com.claytonbc.barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dados necessários para criar um novo agendamento")
public record CreateAppointmentRequest(

        @Schema(
                description = "ID do barbeiro que realizará o atendimento",
                example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long barberId,

        @Schema(
                description = "Data e hora de início do agendamento",
                example = "2026-05-10T14:30:00",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "date-time"
        )
        LocalDateTime startTime,

        @Schema(
                description = "Data e hora de término do agendamento",
                example = "2026-05-10T15:00:00",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "date-time"
        )
        LocalDateTime endTime

) {}