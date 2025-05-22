package com.gianfranco.products.dto.product;

public record CreateProductDTO(
        Long id,
        String name,
        String description,
        Double price,
        Long initialStock
)
{
}
