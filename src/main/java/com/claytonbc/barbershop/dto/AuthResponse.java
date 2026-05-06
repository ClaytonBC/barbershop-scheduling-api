package com.claytonbc.barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo o token JWT")
public record AuthResponse(

        @Schema(
                description = "Token JWT para autenticação nas requisições",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String token

) {}
