package com.gianfranco.products.dto.stock;

import java.time.LocalDateTime;

public record StockDTO(
        Long productId,
        Long quantity,
        LocalDateTime lastUpdate
) {
}
