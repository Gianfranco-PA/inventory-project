package com.gianfranco.products.service;

import com.gianfranco.products.dto.product.ProductDTO;
import com.gianfranco.products.model.Product;

import java.util.List;

public interface IProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductDTO product);

    ProductDTO updateProduct(Long id,ProductDTO product);

    ProductDTO deleteProduct(Long id);
}
