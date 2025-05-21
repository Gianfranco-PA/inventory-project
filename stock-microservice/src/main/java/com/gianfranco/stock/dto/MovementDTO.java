package com.gianfranco.stock.dto;

import java.time.LocalDateTime;

public record MovementDTO(
        Long amount,
        String description,
        LocalDateTime date
) {
}
