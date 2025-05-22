package com.gianfranco.products.map;

import com.gianfranco.products.dto.product.CreateProductDTO;
import com.gianfranco.products.dto.product.ProductDTO;
import com.gianfranco.products.dto.product.ProductStockDTO;
import com.gianfranco.products.dto.stock.StockDTO;
import com.gianfranco.products.model.Product;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public Product toProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price() != null ? productDTO.price() : 0.0);
        return product;
    }

    public Product toProduct(CreateProductDTO productDTO) {
        return toProduct(productDTO.product());
    }

    public ProductStockDTO toProductStockDTO(Product product, StockDTO stock){
        return new ProductStockDTO(toProductDTO(product), stock);
    }

}
