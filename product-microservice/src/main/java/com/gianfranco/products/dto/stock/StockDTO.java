package com.gianfranco.products.dto.stock;

import java.time.LocalDateTime;
import java.util.List;

public record StockDTO(
        Long productId,
        Long quantity,

        //In this service, this property only receives
        //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime lastUpdate,

        List<MovementDTO> movements
) {
}
