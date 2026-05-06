package com.claytonbc.barbershop.dto;

import com.claytonbc.barbershop.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta com os dados do cliente")
public record CustomerResponse(

        @Schema(
                description = "ID do cliente",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome do cliente",
                example = "Clayton Silva"
        )
        String name,

        @Schema(
                description = "Email do cliente",
                example = "clayton@email.com"
        )
        String email,

        @Schema(
                description = "Perfil do usuário",
                example = "CUSTOMER"
        )
        Perfil role

) {
}
