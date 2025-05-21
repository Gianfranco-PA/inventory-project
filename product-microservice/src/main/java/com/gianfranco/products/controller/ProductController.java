package com.gianfranco.products.controller;

import com.gianfranco.products.dto.ProductDTO;
import com.gianfranco.products.map.Mapper;
import com.gianfranco.products.model.Product;
import com.gianfranco.products.service.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final IProductService productService;
    private final Mapper mapper;

    public ProductController(IProductService productService, Mapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts().stream().map(mapper::toProductDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toProductDTO(productService.getProductById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        Product productModel = mapper.toProduct(product);
        Product savedProduct = productService.createProduct(productModel);
        URI location = URI.create("/api/product/" + savedProduct.getId());
        return ResponseEntity.created(location).body(mapper.toProductDTO(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        Product mappedProduct = mapper.toProduct(product);
        return ResponseEntity.ok(mapper.toProductDTO(productService.updateProduct(id, mappedProduct)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toProductDTO(productService.deleteProduct(id)));
    }
}
