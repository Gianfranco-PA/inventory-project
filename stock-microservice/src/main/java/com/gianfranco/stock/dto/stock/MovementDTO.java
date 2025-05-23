package com.gianfranco.stock.dto.stock;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record MovementDTO(
        @NonNull
        Long amount,
        String description,
        LocalDateTime date
) {
}
