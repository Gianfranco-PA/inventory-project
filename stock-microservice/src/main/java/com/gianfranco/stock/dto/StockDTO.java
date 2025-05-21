package com.gianfranco.stock.dto;

import java.time.LocalDateTime;
import java.util.List;

public record StockDTO(
        Long productId,
        Long quantity,
        LocalDateTime lastUpdate,
        List<MovementDTO> movements
) {
}
