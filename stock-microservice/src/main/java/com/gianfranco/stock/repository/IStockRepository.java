package com.gianfranco.stock.repository;

import com.gianfranco.stock.model.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IStockRepository extends CrudRepository<Stock, Long> {
    Optional<Stock> findByProductId(Long productId);
}
