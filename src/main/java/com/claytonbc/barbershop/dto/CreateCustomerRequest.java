package com.claytonbc.barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para criação de um cliente")
public record CreateCustomerRequest(

        @NotBlank
        @Schema(
                description = "Nome completo do cliente",
                example = "Clayton Silva",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,

        @Email
        @NotBlank
        @Schema(
                description = "Email do cliente",
                example = "clayton@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @NotBlank
        @Size(min = 6)
        @Schema(
                description = "Senha do cliente com no mínimo 6 caracteres",
                example = "123456",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 6
        )
        String password

) {
}
