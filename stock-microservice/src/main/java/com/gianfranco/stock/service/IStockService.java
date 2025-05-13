package com.gianfranco.stock.service;

import com.gianfranco.stock.model.Movement;
import com.gianfranco.stock.model.Stock;

import java.util.List;

public interface IStockService {
    List<Stock> getAllStocks();

    Stock getStockByProductId(Long product_id);

    Stock addMovement(Long product_id, Movement movement);
}
