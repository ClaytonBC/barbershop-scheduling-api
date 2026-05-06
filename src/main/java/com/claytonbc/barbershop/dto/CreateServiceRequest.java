package com.claytonbc.barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Dados necessários para criação de um serviço")
public record CreateServiceRequest(

        @NotBlank
        @Schema(
                description = "Nome do serviço",
                example = "Corte de cabelo",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,

        @NotNull
        @DecimalMin("0.0")
        @Schema(
                description = "Preço do serviço",
                example = "35.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal price,

        @NotNull
        @Min(1)
        @Schema(
                description = "Duração do serviço em minutos",
                example = "30",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Integer duration

) {
}