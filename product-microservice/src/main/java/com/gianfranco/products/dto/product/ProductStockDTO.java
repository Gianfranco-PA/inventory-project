package com.gianfranco.products.dto.product;

import com.gianfranco.products.dto.stock.StockDTO;
import org.springframework.lang.NonNull;

public record ProductStockDTO(
        @NonNull
        ProductDTO product,
        @NonNull
        StockDTO stock
) {
}
