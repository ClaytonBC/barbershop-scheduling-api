package com.claytonbc.barbershop.dto;

import java.math.BigDecimal;

public record ServiceResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer duration
) {
}
