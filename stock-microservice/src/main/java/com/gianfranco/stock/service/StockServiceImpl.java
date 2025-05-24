package com.gianfranco.stock.service;

import com.gianfranco.stock.client.ProductClient;
import com.gianfranco.stock.dto.stock.MovementDTO;
import com.gianfranco.stock.dto.stock.StockDTO;
import com.gianfranco.stock.dto.stock.StockMovementsDTO;
import com.gianfranco.stock.map.Mapper;
import com.gianfranco.stock.model.Movement;
import com.gianfranco.stock.model.Stock;
import com.gianfranco.stock.repository.IStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class StockServiceImpl implements IStockService {

    private final IStockRepository stockRepository;
    private final ProductClient productClient;
    private final Mapper mapper;

    public StockServiceImpl(IStockRepository stockRepository, ProductClient productClient ,Mapper mapper) {
        this.stockRepository = stockRepository;
        this.productClient = productClient;
        this.mapper = mapper;
    }

    @Override
    public List<StockDTO> getAllStocks() {
        return StreamSupport.stream(stockRepository.findAll().spliterator(), false)
                .map(mapper::toStockDTO)
                .toList();
    }

    @Override
    public StockDTO getStockByProductId(Long productId) {
        return this.stockRepository.findById(productId).map(mapper::toStockDTO).orElseThrow(
                () -> new IllegalArgumentException("Stock with id " + productId + " not found")
        );
    }

    @Override
    public StockMovementsDTO getStockMovementsByProductId(Long productId) {
        return this.stockRepository.findById(productId).map(mapper::toStockMovementDTO).orElseThrow(
                () -> new IllegalArgumentException("Stock with id " + productId + " not found")
        );
    }

    @Override
    public StockDTO addMovement(Long productId, @Validated MovementDTO movement) {

        try {
            if (!productClient.isProductExists(productId)) {
                throw new IllegalArgumentException("Product with id " + productId + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting product with id " + productId, e);
        }

        Movement mappedMovement = mapper.toMovement(movement);
        Stock stock = this.stockRepository.findById(productId).orElseGet(() -> {
            Stock newStock = new Stock();
            newStock.setProductId(productId);
            return newStock;
        });
        stock.addMovement(mappedMovement);
        Stock savedStock = this.stockRepository.save(stock);
        return mapper.toStockDTO(savedStock);
    }
}
