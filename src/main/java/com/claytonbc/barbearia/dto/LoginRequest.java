package com.claytonbc.barbearia.dto;

public record LoginRequest(
        String email,
        String password
) {
}
