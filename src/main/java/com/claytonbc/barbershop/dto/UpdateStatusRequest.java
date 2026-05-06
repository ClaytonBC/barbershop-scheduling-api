package com.claytonbc.barbershop.dto;

import com.claytonbc.barbershop.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados necessários para atualização do status de um agendamento")
public record UpdateStatusRequest(

        @NotNull
        @Schema(
                description = "Novo status do agendamento",
                example = "CONFIRMED",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Status status

) {
}
