package com.gianfranco.stock.service;

import com.gianfranco.stock.dto.stock.MovementDTO;
import com.gianfranco.stock.dto.stock.StockDTO;

import java.util.List;

public interface IStockService {
    List<StockDTO> getAllStocks();

    StockDTO getStockByProductId(Long productId);

    StockDTO addMovement(Long productId, MovementDTO movement);
}
