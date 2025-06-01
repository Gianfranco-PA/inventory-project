package com.gianfranco.products.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StockMovementsDTO(
        @Schema(description = "Lista de movimientos realizados sobre cierto producto")
        List<MovementDTO> movements
) {
}
