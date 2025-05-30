package com.gianfranco.products;

import com.gianfranco.products.client.StockClient;
import com.gianfranco.products.dto.product.CreateProductDTO;
import com.gianfranco.products.dto.product.ProductDTO;
import com.gianfranco.products.dto.product.ProductStockDTO;
import com.gianfranco.products.dto.product.ProductTrackDTO;
import com.gianfranco.products.dto.stock.MovementDTO;
import com.gianfranco.products.dto.stock.StockDTO;
import com.gianfranco.products.dto.stock.StockMovementsDTO;
import com.gianfranco.products.map.Mapper;
import com.gianfranco.products.model.Product;
import com.gianfranco.products.repository.IProductRepository;
import com.gianfranco.products.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

	@Mock
	private IProductRepository repository;

	@Mock
	private StockClient client;

	@Mock
	private PlatformTransactionManager transactionManager;

	@InjectMocks
	private ProductServiceImpl service;

	private final Mapper mapper = new Mapper();
	private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2025, 5, 30, 12, 0);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		service = new ProductServiceImpl(repository, client, mapper, transactionManager);
	}

	@Test
	void testGetAllProductsSuccess() {
		Product product1 = new Product(1L, "TestProduct1", "A test product", 9.99);
		Product product2 = new Product(2L, "TestProduct2", "A test product", 0.01);
		Product product3 = new Product(3L, "TestProduct3", "A test product", 5.00);

		when(repository.findAll()).thenReturn(List.of(product1, product2, product3));

		List<ProductDTO> products = service.getAllProducts();

		assertNotNull(products);
		assertEquals(3, products.size());

		assertEquals(1L, products.get(0).id());
		assertEquals("TestProduct1", products.get(0).name());

		assertEquals(2L, products.get(1).id());
		assertEquals("TestProduct2", products.get(1).name());

		assertEquals(3L, products.get(2).id());
		assertEquals("TestProduct3", products.get(2).name());
	}


	@Test
	void testGetProductByIdSuccess() {
		Product product = new Product(1L, "TestProduct", "A test product", 9.99);

		when(repository.findById(1L)).thenReturn(Optional.of(product));

		ProductDTO productDTO = service.getProductById(1L);

		assertNotNull(productDTO);
		assertEquals(1L, productDTO.id());
		assertEquals("TestProduct", productDTO.name());
		assertEquals("A test product", productDTO.description());
		assertEquals(9.99, productDTO.price());
	}

	@Test
	void testGetProductByIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getProductById(1L));

		assertEquals("Product with id 1 not found", ex.getMessage());
	}

	@Test
	void testGetProductStockByIdSuccess() {
		Product product = new Product(1L, "TestProduct", "A test product", 9.99);
		StockDTO stockDTO = new StockDTO(1L, 10L, FIXED_TIME);

		when(repository.findById(1L)).thenReturn(Optional.of(product));
		when(client.getStock(1L)).thenReturn(stockDTO);

		ProductStockDTO productStockDTO = service.getProductStockById(1L);

		assertNotNull(productStockDTO);
		assertEquals(1L, productStockDTO.product().id());
		assertEquals("TestProduct", productStockDTO.product().name());
		assertEquals("A test product", productStockDTO.product().description());
		assertEquals(9.99, productStockDTO.product().price());

		assertEquals(10L, productStockDTO.stock().quantity());
		assertEquals(FIXED_TIME, productStockDTO.stock().lastUpdate());
	}

	@Test
	void testGetProductStockByIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getProductStockById(1L));

		assertEquals("Product with id 1 not found", ex.getMessage());
	}

	@Test
	void testGetProductTrackByIdSuccess(){
		Product product = new Product(2L, "TestProduct", "A test product", 9.99);
		StockDTO stockDTO = new StockDTO(2L, 10L, FIXED_TIME);

		StockMovementsDTO movementsDTO = new StockMovementsDTO(List.of(
				new MovementDTO(4L, "Move1", FIXED_TIME),
				new MovementDTO(7L, "Move2", FIXED_TIME)
		));

		when(repository.findById(2L)).thenReturn(Optional.of(product));
		when(client.getStock(2L)).thenReturn(stockDTO);
		when(client.getMovements(2L)).thenReturn(movementsDTO);

		ProductTrackDTO productTrackDTO = service.getProductTrackById(2L);

		assertNotNull(productTrackDTO);
		assertEquals(2L, productTrackDTO.product().id());
		assertEquals("TestProduct", productTrackDTO.product().name());

		assertEquals(10L, productTrackDTO.stock().quantity());
		assertEquals(FIXED_TIME, productTrackDTO.stock().lastUpdate());

		assertEquals(2, productTrackDTO.movements().size());
		assertEquals("Move1", productTrackDTO.movements().get(0).description());
		assertEquals("Move2", productTrackDTO.movements().get(1).description());
	}


	@Test
	void testCreateProductSuccess() {
		CreateProductDTO input = new CreateProductDTO(
				new ProductDTO(null, "TestProduct", "A test product", 9.99),
				10L
		);
		Product saved = new Product(2L, "TestProduct", "A test product", 9.99);

		when(repository.save(any(Product.class)))
				.thenReturn(saved);
		when(client.createInitially(eq(2L),eq(10L)))
				.thenReturn(new StockDTO(2L,10L, FIXED_TIME));

		ProductStockDTO result = service.createProduct(input);

		assertNotNull(result);
		assertEquals(2L, result.product().id());
		assertEquals(10L, result.stock().quantity());
	}

	@Test
	void testCreateProductSaveFailure() {
		CreateProductDTO input = new CreateProductDTO(
				new ProductDTO(null, "TestProduct", "A test product", 9.99),
				10L
		);

		when(repository.save(any(Product.class)))
				.thenReturn(null);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createProduct(input));
		assertEquals("Error saving product", ex.getMessage());
	}

	@Test
	void testCreateProductStockFailure() {
		Product saved = new Product(2L, "TestProduct", "A test product", 9.99);
		CreateProductDTO input = new CreateProductDTO(
				new ProductDTO(null, "TestProduct", "A test product", 9.99),
				10L
		);

		when(repository.save(any(Product.class)))
				.thenReturn(saved);
		when(client.createInitially(eq(2L),eq(10L)))
				.thenThrow(new RuntimeException("Error creating stock for product TestProduct"));

		RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createProduct(input));
		assertEquals("Error creating stock for product TestProduct", ex.getMessage());
	}

	@Test
	void testUpdateProductSuccess() {
		ProductDTO input = new ProductDTO(null, "UpdatedTestProduct", "An updated test product", 9.99);
		Product saved = new Product(3L, "TestProduct", "A test product", 9.99);
		Product updated = new Product(3L, "UpdatedTestProduct", "An updated test product", 9.99);

		when(repository.findById(3L))
				.thenReturn(Optional.of(saved));
		when(repository.save(any(Product.class)))
				.thenReturn(updated);

		ProductDTO result = service.updateProduct(3L, input);

		assertNotNull(result);
		assertEquals(3L, result.id());
		assertEquals("UpdatedTestProduct", result.name());
		assertEquals("An updated test product", result.description());
		assertEquals(9.99, result.price());
	}

	@Test
	void testUpdateProductNotFound() {
		ProductDTO input = new ProductDTO(null, "UpdatedTestProduct", "An updated test product", 9.99);
		when(repository.findById(3L))
				.thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.updateProduct(3L, input));
		assertEquals("Product with id 3 not found", ex.getMessage());
	}

	@Test
	void testDeleteProductSuccess() {
		Product saved = new Product(3L, "TestProduct", "A test product", 9.99);
		when(repository.findById(3L))
				.thenReturn(Optional.of(saved));

		ProductDTO result = service.deleteProduct(3L);

		assertNotNull(result);
		assertEquals(3L, result.id());
	}

	@Test
	void testDeleteProductNotFound() {
		when(repository.findById(3L))
				.thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.deleteProduct(3L));
		assertEquals("Product with id 3 not found", ex.getMessage());
	}



}
