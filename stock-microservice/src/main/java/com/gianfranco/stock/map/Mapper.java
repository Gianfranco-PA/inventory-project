package com.gianfranco.stock.map;

import com.gianfranco.stock.dto.stock.MovementDTO;
import com.gianfranco.stock.dto.stock.StockDTO;
import com.gianfranco.stock.dto.stock.StockMovementsDTO;
import com.gianfranco.stock.model.Movement;
import com.gianfranco.stock.model.Stock;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Mapper {

    public MovementDTO toMovementDTO(Movement movement) {
        return new MovementDTO(
                movement.getAmount(),
                movement.getDescription(),
                movement.getDate());

    }

    public StockDTO toStockDTO(Stock stock) {
        return new StockDTO(
                stock.getProductId(),
                stock.getQuantity(),
                stock.getLastUpdate());
    }

    public StockMovementsDTO toStockMovementDTO(Stock stock) {
        return new StockMovementsDTO(
                stock.getMovements().stream().map(this::toMovementDTO).toList());
    }

    public Movement toMovement(MovementDTO movementDTO) {
        Movement movement = new Movement();
        movement.setAmount(movementDTO.amount());
        movement.setDescription(movementDTO.description());
        movement.setDate(movementDTO.date() != null ? movementDTO.date(): LocalDateTime.now());
        return movement;
    }
}
