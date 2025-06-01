package com.gianfranco.products.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

public record CreateProductDTO(
        @Schema(description = "Datos del producto a crear")
        @NonNull
        ProductDTO product,

        @NonNull
        @Schema(description = "Cantidad de ingreso del producto", example = "35")
        Long initialStock
)
{
}
