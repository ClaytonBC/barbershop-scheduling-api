package com.claytonbc.barbershop.dto;

import com.claytonbc.barbershop.enums.Status;

public record UpdateStatusRequest(
        Status status
) {
}
