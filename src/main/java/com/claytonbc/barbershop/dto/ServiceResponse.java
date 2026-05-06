package com.claytonbc.barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Resposta com os dados de um serviço")
public record ServiceResponse(

        @Schema(
                description = "ID do serviço",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome do serviço",
                example = "Corte de cabelo"
        )
        String name,

        @Schema(
                description = "Preço do serviço",
                example = "35.00"
        )
        BigDecimal price,

        @Schema(
                description = "Duração do serviço em minutos",
                example = "30"
        )
        Integer duration

) {
}
