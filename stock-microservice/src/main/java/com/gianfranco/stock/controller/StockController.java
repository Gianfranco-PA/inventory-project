package com.gianfranco.stock.controller;

import com.gianfranco.stock.model.Movement;
import com.gianfranco.stock.model.Stock;
import com.gianfranco.stock.service.IStockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final IStockService stockService;

    public StockController(IStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public Stock getStockByProductId(@PathVariable Long id) {
        return stockService.getStockByProductId(id);
    }

    @PostMapping("/{id}")
    public Stock addMovement(@PathVariable Long id, @RequestBody Movement movement) {
        return this.stockService.addMovement(id, movement);
    }


}
