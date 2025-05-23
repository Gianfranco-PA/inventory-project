package com.gianfranco.products.dto.product;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record ProductDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        @NonNull
        String name,
        String description,
        Double price
) {
}
