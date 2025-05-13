package com.gianfranco.stock.service;

import com.gianfranco.stock.model.Movement;
import com.gianfranco.stock.model.Stock;
import com.gianfranco.stock.repository.IStockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockServiceImpl implements IStockService {

    private final IStockRepository stockRepository;

    public StockServiceImpl(IStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getAllStocks() {
        return (List<Stock>) this.stockRepository.findAll();
    }

    @Override
    public Stock getStockByProductId(Long product_id) {
        return this.stockRepository.findById(product_id).orElse(null);
    }

    @Override
    public Stock addMovement(Long product_id, Movement movement) {
        Stock stock = this.stockRepository.findById(product_id).orElseGet(() -> {
            Stock newStock = new Stock();
            newStock.setProductId(product_id);
            newStock.setQuantity(0L);
            return newStock;
        });
        movement.setStock(stock);
        stock.addMovement(movement);
        stock.setLastUpdate(LocalDateTime.now());
        this.stockRepository.save(stock);
        return stock;
    }
}
