package com.claytonbc.barbershop.dto;

import java.math.BigDecimal;

public record CreateServiceRequest(
        String name,
        BigDecimal price,
        Integer duration
) {
}
