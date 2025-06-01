package com.gianfranco.products.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record StockDTO(
        @Schema(description = "Id del producto", example = "3", accessMode = Schema.AccessMode.WRITE_ONLY)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        Long productId,

        @Schema(description = "Cantidad existente del producto", example = "60")
        Long quantity,

        @Schema(description = "Ultima actualización realizada sobre el stock", example = "2025-05-22T20:53:03.130581")
        LocalDateTime lastUpdate
) {
}
