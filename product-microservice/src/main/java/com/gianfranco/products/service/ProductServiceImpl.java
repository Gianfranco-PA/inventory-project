package com.gianfranco.products.service;

import com.gianfranco.products.client.StockClient;
import com.gianfranco.products.dto.product.CreateProductDTO;
import com.gianfranco.products.dto.product.ProductDTO;
import com.gianfranco.products.dto.product.ProductStockDTO;
import com.gianfranco.products.dto.stock.StockDTO;
import com.gianfranco.products.map.Mapper;
import com.gianfranco.products.model.Product;
import com.gianfranco.products.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final StockClient stockClient;
    private final Mapper mapper;

    public ProductServiceImpl(IProductRepository productRepository, StockClient stockClient, Mapper mapper) {
        this.productRepository = productRepository;
        this.stockClient = stockClient;
        this.mapper = mapper;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(mapper::toProductDTO)
                .toList();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(mapper::toProductDTO).orElse(null);
    }

    @Override
    public ProductStockDTO createProduct(CreateProductDTO product) {
        Product savedProduct = productRepository.save(mapper.toProduct(product));

        StockDTO savedStock;
        try {
            savedStock = stockClient.createInitially(savedProduct.getId(), product.initialStock());
        } catch (Exception e) {
            productRepository.deleteById(savedProduct.getId());
            throw new RuntimeException("Error creating stock for product " + savedProduct.getName(), e);
        }

        return mapper.toProductStockDTO(savedProduct, savedStock);
    }

    @Override
    public ProductDTO updateProduct(Long id ,ProductDTO product) {
        Product mappedProduct = mapper.toProduct(product);
        mappedProduct.setId(id);
        productRepository.save(mappedProduct);
        return mapper.toProductDTO(mappedProduct);
    }

    @Override
    public ProductDTO deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
        return mapper.toProductDTO(product);
    }
}
