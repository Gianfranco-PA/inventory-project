package com.gianfranco.stock.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductDTO(
        @Schema(description = "ID generado automáticamente del producto", example = "1")
        Long id,

        @Schema(description = "Nombre del producto", example = "Camisa de algodón")
        String name,

        @Schema(description = "Descripción ampliada del producto", example = "100% algodón, talla M")
        String description,

        @Schema(description = "Precio en soles", example = "25.0")
        Double price
) {
}
