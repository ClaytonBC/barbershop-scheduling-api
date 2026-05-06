package com.claytonbc.barbershop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(

        @NotBlank
        String name,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6)
        String password
) {
}
