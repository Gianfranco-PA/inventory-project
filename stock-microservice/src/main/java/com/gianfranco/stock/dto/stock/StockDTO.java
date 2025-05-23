package com.gianfranco.stock.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record StockDTO(
        Long productId,
        Long quantity,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime lastUpdate,

        List<MovementDTO> movements
) {
}
