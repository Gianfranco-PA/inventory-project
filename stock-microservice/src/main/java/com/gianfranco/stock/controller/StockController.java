package com.gianfranco.stock.controller;

import com.gianfranco.stock.dto.MovementDTO;
import com.gianfranco.stock.dto.StockDTO;
import com.gianfranco.stock.map.Mapper;
import com.gianfranco.stock.model.Movement;
import com.gianfranco.stock.model.Stock;
import com.gianfranco.stock.service.IStockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final IStockService stockService;
    private final Mapper mapper;

    public StockController(IStockService stockService, Mapper mapper) {
        this.stockService = stockService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<StockDTO> getAllStocks() {
        return stockService.getAllStocks().stream().map(mapper::toStockDTO).toList();
    }

    @GetMapping("/{id}")
    public StockDTO getStockByProductId(@PathVariable Long id) {
        return mapper.toStockDTO(stockService.getStockByProductId(id));
    }

    @PostMapping("/{id}")
    public StockDTO addMovement(@PathVariable Long id, @RequestBody MovementDTO movement) {
        Movement mappedMovement = mapper.toMovement(movement);
        Stock stock = stockService.addMovement(id, mappedMovement);
        return mapper.toStockDTO(stock);
    }


}
