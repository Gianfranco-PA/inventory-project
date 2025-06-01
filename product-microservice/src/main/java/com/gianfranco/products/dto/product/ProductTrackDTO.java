package com.gianfranco.products.dto.product;

import com.gianfranco.products.dto.stock.MovementDTO;
import com.gianfranco.products.dto.stock.StockDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

import java.util.List;

public record ProductTrackDTO(
        @Schema(description = "Datos del producto")
        @NonNull
        ProductDTO product,

        @Schema(description = "Stock del producto")
        @NonNull
        StockDTO stock,
        @NonNull
        @Schema(description = "Traza de movimientos del producto")
        List<MovementDTO> movements
) {
}
