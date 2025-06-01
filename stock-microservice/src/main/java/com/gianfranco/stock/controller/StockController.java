package com.gianfranco.stock.controller;

import com.gianfranco.stock.dto.stock.MovementDTO;
import com.gianfranco.stock.dto.stock.StockDTO;
import com.gianfranco.stock.dto.stock.StockMovementsDTO;
import com.gianfranco.stock.service.IStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock Service", description = "Operaciones de consulta y movimiento de stock")
public class StockController {

    private final IStockService stockService;

    public StockController(IStockService stockService) {
        this.stockService = stockService;
    }

    @Operation(
            summary = "Obtener todas las entradas de stock",
            description = "Retorna una lista con el stock actual (cantidad y última actualización) de todos los productos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de stock obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StockDTO.class, type = "array"))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @Operation(
            summary = "Obtener stock por ID de producto",
            description = "Retorna la información de stock (cantidad y última actualización) para el producto especificado",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto cuyo stock se desea consultar", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock del producto obtenido",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StockDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Stock no encontrado para el producto indicado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockByProductId(id));
    }

    @Operation(
            summary = "Obtener movimientos de stock por ID de producto",
            description = "Retorna el historial de movimientos de stock (entradas y salidas) para el producto especificado",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto cuyos movimientos se desean consultar", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movimientos de stock obtenidos correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StockMovementsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "No se encontró stock o movimientos para el producto indicado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}/movements")
    public ResponseEntity<StockMovementsDTO> getStockMovementsByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockMovementsByProductId(id));
    }

    @Operation(
            summary = "Agregar un movimiento de stock a un producto",
            description = "Agrega un movimiento (entrada o salida) al stock del producto indicado y retorna el stock actualizado",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto al que se agregará el movimiento", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del movimiento (cantidad, descripción y fecha opcional)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovementDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movimiento agregado y stock actualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StockDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado para agregar movimiento"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping("/{id}")
    public ResponseEntity<StockDTO> addMovement(@PathVariable Long id, @RequestBody MovementDTO movement) {
        return ResponseEntity.ok(stockService.addMovement(id, movement));
    }


}
