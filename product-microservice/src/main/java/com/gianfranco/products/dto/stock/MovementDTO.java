package com.gianfranco.products.dto.stock;

import java.time.LocalDateTime;

public record MovementDTO(
        Long amount,
        String description,
        LocalDateTime date
) {
}
