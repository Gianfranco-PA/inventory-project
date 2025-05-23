package com.gianfranco.stock.controller;

import com.gianfranco.stock.dto.stock.MovementDTO;
import com.gianfranco.stock.dto.stock.StockDTO;
import com.gianfranco.stock.service.IStockService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockByProductId(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<StockDTO> addMovement(@PathVariable Long id, @RequestBody MovementDTO movement) {
        return ResponseEntity.ok(stockService.addMovement(id, movement));
    }


}
