package com.gianfranco.products.map;

import com.gianfranco.products.dto.product.ProductDTO;
import com.gianfranco.products.model.Product;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public Product toProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        return product;
    }
}
