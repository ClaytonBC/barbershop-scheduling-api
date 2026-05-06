package com.claytonbc.barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados necessários para autenticação do usuário")
public record LoginRequest(

        @Email
        @NotBlank
        @Schema(
                description = "Email do usuário",
                example = "clayton@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @NotBlank
        @Schema(
                description = "Senha do usuário",
                example = "123456",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String password

) {
}