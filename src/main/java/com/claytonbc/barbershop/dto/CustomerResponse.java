package com.claytonbc.barbershop.dto;

import com.claytonbc.barbershop.enums.Perfil;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        Perfil role
) {
}
