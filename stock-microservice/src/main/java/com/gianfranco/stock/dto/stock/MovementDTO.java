package com.gianfranco.stock.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record MovementDTO(
        @Schema(description = "Variación de cantidad del producto", example = "5")
        @NonNull
        Long amount,

        @Schema(description = "Descripción de la variación realizada", example = "Desembolso")
        String description,

        @Schema(description = "Fecha de realización del movimiento", example = "2025-05-22T20:53:03.130581")
        LocalDateTime date
) {
}
