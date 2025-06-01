package com.gianfranco.products.dto.product;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

public record ProductDTO(
        @Schema(description = "ID generado automáticamente del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @Schema(description = "Nombre del producto", example = "Camisa de algodón")
        @NonNull
        String name,

        @Schema(description = "Descripción ampliada del producto", example = "100% algodón, talla M")
        String description,

        @Schema(description = "Precio en soles", example = "25.0")
        Double price
) {
}
