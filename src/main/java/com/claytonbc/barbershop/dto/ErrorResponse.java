package com.claytonbc.barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta padrão para erros da API")
public record ErrorResponse(

        @Schema(
                description = "Data e hora em que o erro ocorreu",
                example = "2026-05-10T14:30:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime timestamp,

        @Schema(
                description = "Código HTTP do erro",
                example = "404"
        )
        int status,

        @Schema(
                description = "Mensagem detalhando o erro",
                example = "Cliente não encontrado"
        )
        String message

) {
}
