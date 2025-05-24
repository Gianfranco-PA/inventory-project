package com.gianfranco.products.dto.product;

import com.gianfranco.products.dto.stock.MovementDTO;
import com.gianfranco.products.dto.stock.StockDTO;
import org.springframework.lang.NonNull;

import java.util.List;

public record ProductTrackDTO(
        @NonNull
        ProductDTO product,
        @NonNull
        StockDTO stock,
        @NonNull
        List<MovementDTO> movements
) {
}
