package com.gianfranco.stock.service;

import com.gianfranco.stock.dto.MovementDTO;
import com.gianfranco.stock.dto.StockDTO;

import java.util.List;

public interface IStockService {
    List<StockDTO> getAllStocks();

    StockDTO getStockByProductId(Long productId);

    StockDTO addMovement(Long productId, MovementDTO movement);
}
