package com.gianfranco.stock;

import com.gianfranco.stock.client.ProductClient;
import com.gianfranco.stock.dto.stock.MovementDTO;
import com.gianfranco.stock.dto.stock.StockDTO;
import com.gianfranco.stock.dto.stock.StockMovementsDTO;
import com.gianfranco.stock.map.Mapper;
import com.gianfranco.stock.model.Movement;
import com.gianfranco.stock.model.Stock;
import com.gianfranco.stock.repository.IStockRepository;
import com.gianfranco.stock.service.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StockServiceImplTest {

	@Mock
	private IStockRepository repository;

	@Mock
	private ProductClient client;

	@InjectMocks
	private StockServiceImpl service;

	private final Mapper mapper = new Mapper();
	private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2025, 5, 30, 12, 0);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		service = new StockServiceImpl(repository, client, mapper);
	}


	@Test
	void testGetAllStocksSuccess() {
		Stock stock1 = new Stock(1L, 1L, null, 10L, FIXED_TIME);
		Stock stock2 = new Stock(2L, 2L, null, -2L, FIXED_TIME);
		Stock stock3 = new Stock(3L, 3L, null, 5L, FIXED_TIME);


		when(repository.findAll()).thenReturn(List.of(stock1, stock2, stock3));

		List<StockDTO> stocks = service.getAllStocks();

		assertNotNull(stocks);
		assertEquals(3, stocks.size());

		assertEquals(1L, stocks.get(0).productId());
		assertEquals(10L, stocks.get(0).quantity());

		assertEquals(2L, stocks.get(1).productId());
		assertEquals(-2L, stocks.get(1).quantity());
	}

	@Test
	void testGetStockByProductIdSuccess() {
		Stock stock = new Stock(1L, 2L, 10L, FIXED_TIME);

		when(repository.findById(2L)).thenReturn(Optional.of(stock));

		StockDTO stockDTO = service.getStockByProductId(2L);

		assertNotNull(stockDTO);
		assertEquals(2L, stockDTO.productId());
		assertEquals(10L, stockDTO.quantity());
	}

	@Test
	void testGetStockByProductIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getStockByProductId(1L));

		assertEquals("Stock with id 1 not found", ex.getMessage());
	}

	@Test
	void testGetStockMovementsByProductIdSuccess() {
		Stock stock = new Stock(1L, 3L, 10L, FIXED_TIME);

		Movement movement1 = new Movement(1L, null, 10L, "Test movement", FIXED_TIME);
		Movement movement2 = new Movement(2L, null, -5L, "Another movement", FIXED_TIME);
		Movement movement3 = new Movement(3L, null, 10L, "Another movement", FIXED_TIME);

		stock.addMovement(movement1);
		stock.addMovement(movement2);
		stock.addMovement(movement3);

		when(repository.findById(3L)).thenReturn(Optional.of(stock));

		StockMovementsDTO stockMovementsDTO = service.getStockMovementsByProductId(3L);

		assertNotNull(stockMovementsDTO);
		assertEquals(3, stockMovementsDTO.movements().size());

		assertEquals(10L, stockMovementsDTO.movements().get(0).amount());
		assertEquals("Test movement", stockMovementsDTO.movements().get(0).description());

		assertEquals(-5L, stockMovementsDTO.movements().get(1).amount());
		assertEquals("Another movement", stockMovementsDTO.movements().get(1).description());

		assertEquals(10L, stockMovementsDTO.movements().get(2).amount());
		assertEquals("Another movement", stockMovementsDTO.movements().get(2).description());
	}

	@Test
	void testGetStockMovementsByProductIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getStockMovementsByProductId(1L));

		assertEquals("Stock with id 1 not found", ex.getMessage());
	}

	@Test
	void testCreateStockSuccess() {
		MovementDTO movementDTO = new MovementDTO(10L, "Test movement", FIXED_TIME);

		Stock saved = new Stock(1L, 1L, 0L, FIXED_TIME);
		Movement movement = new Movement(1L, null, 10L, "Test movement", FIXED_TIME);
		saved.addMovement(movement);

		when(repository.save(any(Stock.class)))
				.thenReturn(saved);
		when(client.isProductExists(1L))
				.thenReturn(true);

		StockDTO result = service.addMovement(1L, movementDTO);

		assertNotNull(result);
		assertEquals(1L, result.productId());
		assertEquals(10L, result.quantity());
	}

	@Test
	void testCreateStockSaveFailure() {
		MovementDTO movementDTO = new MovementDTO(10L, "Test movement", FIXED_TIME);

		when(client.isProductExists(1L))
				.thenReturn(false);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> service.addMovement(1L, movementDTO));
		assertEquals("Error getting product with id 1", ex.getMessage());
	}

	@Test
	void testDeleteStockByProductIdWhenExists() {
		Stock existing = new Stock(5L, 5L, 50L, FIXED_TIME);
		when(repository.findByProductId(5L)).thenReturn(Optional.of(existing));

		StockDTO result = service.deleteStockByProductId(5L);

		assertEquals(result.productId(), existing.getProductId());

		verify(repository).delete(existing);
	}

	@Test
	void testDeleteStockByProductIdWhenNotExists() {
		when(repository.findByProductId(99L)).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.deleteStockByProductId(99L));

		assertEquals("Stock with id 99 not found", ex.getMessage());

		verify(repository, never()).delete(any());
	}
}
