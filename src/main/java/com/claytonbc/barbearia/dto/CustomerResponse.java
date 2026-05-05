package com.claytonbc.barbearia.dto;

import com.claytonbc.barbearia.enums.Perfil;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        Perfil role
) {
}
