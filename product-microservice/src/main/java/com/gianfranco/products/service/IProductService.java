package com.gianfranco.products.service;

import com.gianfranco.products.model.Product;

import java.util.List;

public interface IProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    Product updateProduct(Long id,Product product);

    void deleteProduct(Long id);
}
