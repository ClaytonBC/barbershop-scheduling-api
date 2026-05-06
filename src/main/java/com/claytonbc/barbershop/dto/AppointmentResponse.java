package com.claytonbc.barbershop.dto;

import com.claytonbc.barbershop.enums.Status;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta com os dados de um agendamento")
public record AppointmentResponse(

        @Schema(description = "ID do agendamento", example = "1")
        Long id,

        @Schema(
                description = "Data e hora de início do agendamento",
                example = "2026-05-10T14:30:00"
        )
        LocalDateTime startTime,

        @Schema(
                description = "Data e hora de término do agendamento",
                example = "2026-05-10T15:00:00"
        )
        LocalDateTime endTime,

        @Schema(description = "ID do cliente", example = "10")
        Long clientId,

        @Schema(description = "ID do barbeiro", example = "5")
        Long barberId,

        @Schema(
                description = "Status do agendamento",
                example = "SCHEDULED"
        )
        Status status
) {}