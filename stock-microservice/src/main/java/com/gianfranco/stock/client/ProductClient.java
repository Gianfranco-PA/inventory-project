package com.gianfranco.stock.client;

import com.gianfranco.stock.dto.product.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String url;

    public ProductClient(RestTemplateBuilder restTemplate, @Value("${product.service.url}") String url) {
        this.restTemplate = restTemplate.build();
        this.url = url;
    }

    public boolean isProductExists(Long productId) {
        ProductDTO product = restTemplate.getForObject(url + "/api/product/{id}", ProductDTO.class, productId);
        return product != null && Objects.equals(product.id(), productId);
    }
}
