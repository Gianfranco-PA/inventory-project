package com.gianfranco.stock.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record StockDTO(
        @JsonIgnore
        Long productId,
        Long quantity,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime lastUpdate
) {
}
