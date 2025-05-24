package com.gianfranco.products.client;

import com.gianfranco.products.dto.stock.MovementDTO;
import com.gianfranco.products.dto.stock.StockDTO;
import com.gianfranco.products.dto.stock.StockMovementsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class StockClient {

    private final RestTemplate restTemplate;
    private final String url;

    public StockClient(RestTemplateBuilder restTemplate, @Value("${stock.service.url}") String url) {
        this.restTemplate = restTemplate.build();
        this.url = url;
    }

    public StockDTO createInitially(Long productId, long quantity) {
        MovementDTO movementDTO = new MovementDTO(quantity, "Initial stock", LocalDateTime.now());
        return restTemplate.postForObject(url + "/{id}", movementDTO, StockDTO.class, productId);
    }

    public StockDTO getStock(Long productId) {
        return restTemplate.getForObject(url + "/{id}", StockDTO.class, productId);
    }

    public StockMovementsDTO getMovements(Long productId) {
        return restTemplate.getForObject(url + "/{id}/movements", StockMovementsDTO.class, productId);
    }
}
