package com.gianfranco.products.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record StockDTO(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        Long productId,
        Long quantity,
        LocalDateTime lastUpdate
) {
}
