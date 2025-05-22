package com.gianfranco.products.dto.product;

import com.gianfranco.products.dto.stock.StockDTO;

public record ProductStockDTO(
        ProductDTO product,
        StockDTO stock
) {
}
