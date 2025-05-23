package com.gianfranco.products.dto.product;

import org.springframework.lang.NonNull;

public record CreateProductDTO(
        @NonNull
        ProductDTO product,
        @NonNull
        Long initialStock
)
{
}
