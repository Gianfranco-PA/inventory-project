package com.gianfranco.stock.dto.stock;

import java.util.List;

public record StockMovementsDTO(
        List<MovementDTO> movements
) {
}
