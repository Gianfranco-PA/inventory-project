package com.gianfranco.stock.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record MovementDTO(
        @NonNull
        Long amount,
        @NonNull
        String description,
        @NonNull
        LocalDateTime date
) {
}
