package com.gianfranco.stock.dto.product;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Double price
) {
}
