package com.claytonbc.barbershop.dto;

public record LoginRequest(
        String email,
        String password
) {
}
