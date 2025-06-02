package com.gianfranco.stock.service;

import com.gianfranco.stock.dto.stock.MovementDTO;
import com.gianfranco.stock.dto.stock.StockDTO;
import com.gianfranco.stock.dto.stock.StockMovementsDTO;

import java.util.List;

public interface IStockService {
    List<StockDTO> getAllStocks();

    StockDTO getStockByProductId(Long productId);

    StockMovementsDTO getStockMovementsByProductId(Long productId);

    StockDTO addMovement(Long productId, MovementDTO movement);

    StockDTO deleteStockByProductId(Long productId);
}
