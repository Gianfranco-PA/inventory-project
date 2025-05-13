package com.gianfranco.stock.repository;

import com.gianfranco.stock.model.Stock;
import org.springframework.data.repository.CrudRepository;

public interface IStockRepository extends CrudRepository<Stock, Long> {
}
