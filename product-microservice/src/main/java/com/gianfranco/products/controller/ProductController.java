package com.gianfranco.products.controller;

import com.gianfranco.products.dto.product.CreateProductDTO;
import com.gianfranco.products.dto.product.ProductDTO;
import com.gianfranco.products.dto.product.ProductStockDTO;
import com.gianfranco.products.dto.product.ProductTrackDTO;
import com.gianfranco.products.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product Service", description = "Operaciones CRUD y de stock para productos")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Obtener todos los productos",
            description = "Retorna una lista de todos los productos registrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class,
                                            type = "array"))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(
            summary = "Obtener un producto por ID",
            description = "Retorna un solo producto identificado por su ID",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto a buscar", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(
            summary = "Obtener stock de un producto",
            description = "Retorna la información de stock (cantidad y última actualización) para el producto indicado",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto cuyo stock se desea consultar", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock obtenido correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductStockDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto o stock no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}/stock")
    public ResponseEntity<ProductStockDTO> getProductStockById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductStockById(id));
    }

    @Operation(
            summary = "Obtener trazabilidad de un producto",
            description = "Retorna la información de producto, stock y movimientos de stock para el producto indicado",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto a rastrear", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Información de trazabilidad obtenida",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductTrackDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}/track")
    public ResponseEntity<ProductTrackDTO> getProductTrackById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductTrackById(id));
    }

    @Operation(
            summary = "Crear un nuevo producto con stock inicial",
            description = "Recibe un objeto CreateProductDTO (datos del producto + stock inicial) y crea un producto y su stock asociado",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un producto y asignar stock inicial",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateProductDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductStockDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping
    public ResponseEntity<ProductStockDTO> createProduct(@RequestBody @Validated CreateProductDTO product) {
        ProductStockDTO savedProduct = productService.createProduct(product);
        URI location = URI.create("/api/product/" + savedProduct.product().id());
        return ResponseEntity.created(location).body(savedProduct);
    }

    @Operation(
            summary = "Actualizar un producto existente",
            description = "Actualiza los datos (nombre, descripción, precio) de un producto existente identificado por su ID",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto a actualizar", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del producto (sin ID)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody @Validated ProductDTO product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina el producto identificado por su ID y retorna los datos del producto eliminado",
            parameters = {
                    @Parameter(name = "id", description = "ID del producto a eliminar", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", example = "1"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
