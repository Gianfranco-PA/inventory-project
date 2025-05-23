package com.gianfranco.products.service;

import com.gianfranco.products.dto.product.CreateProductDTO;
import com.gianfranco.products.dto.product.ProductDTO;
import com.gianfranco.products.dto.product.ProductStockDTO;

import java.util.List;

public interface IProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    ProductStockDTO createProduct(CreateProductDTO product);

    ProductDTO updateProduct(Long id,ProductDTO product);

    ProductDTO deleteProduct(Long id);
}
