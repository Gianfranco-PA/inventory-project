package com.gianfranco.products.dto.stock;

import java.util.List;

public record StockMovementsDTO(
        List<MovementDTO> movements
) {
}
