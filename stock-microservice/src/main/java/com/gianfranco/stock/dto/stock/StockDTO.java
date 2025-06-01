package com.gianfranco.stock.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record StockDTO(
        @JsonIgnore
        Long productId,

        @Schema(description = "Cantidad existente del producto", example = "60")
        Long quantity,

        @Schema(description = "Ultima actualización realizada sobre el stock", example = "2025-05-22T20:53:03.130581", accessMode = Schema.AccessMode.READ_ONLY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime lastUpdate
) {
}
