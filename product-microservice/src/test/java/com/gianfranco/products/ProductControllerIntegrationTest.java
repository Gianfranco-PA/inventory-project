package com.gianfranco.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gianfranco.products.dto.product.CreateProductDTO;
import com.gianfranco.products.dto.product.ProductDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("invsis")
            .withUsername("admin")
            .withPassword("admin");

    private static MockWebServer mockStockServer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static Long createdProductId;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // StockClient
        registry.add("stock.service.url",
                () -> mockStockServer.url("/api/stock").toString()
        );
    }

    @BeforeAll
    static void startMockServer() throws Exception {
        mockStockServer = new MockWebServer();
        mockStockServer.start();
    }

    @AfterAll
    static void shutdownMockServer() throws Exception {
        mockStockServer.shutdown();
    }

    @Test
    @Order(1)
    void testCreateProductAndFetchStock() throws Exception {
        mockStockServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                { "productId": 1, "quantity": 60, "lastUpdate": "2025-05-30T12:00:00" }
            """));

        CreateProductDTO dto = new CreateProductDTO(
                new ProductDTO(null, "Test product", "Desc", 20.0),
                60L
        );
        String json = objectMapper.writeValueAsString(dto);

        MvcResult r = mockMvc.perform(post("/api/product")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product.name").value("Test product"))
                .andExpect(jsonPath("$.stock.quantity").value(60))
                .andReturn();

        String location = r.getResponse().getHeader("Location"); // "/api/product/1"
        createdProductId = Long.valueOf(location.substring(location.lastIndexOf('/') + 1));
    }

    @Test
    @Order(2)
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test product"))
                .andExpect(jsonPath("$[0].description").value("Desc"))
                .andExpect(jsonPath("$[0].price").value(20.0));
    }

    @Test
    @Order(3)
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/api/product/{id}", createdProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test product"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    @Order(4)
    void testGetProductStock() throws Exception {
        mockStockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                { "productId": 1, "quantity": 100, "lastUpdate": "2025-05-30T12:00:00" }
            """));

        mockMvc.perform(get("/api/product/{id}/stock", createdProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.name").value("Test product"))
                .andExpect(jsonPath("$.stock.quantity").value(100));
    }

    @Test
    @Order(5)
    void testGetProductTrack() throws Exception {
        mockStockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                { "productId": 1, "quantity": 100, "lastUpdate": "2025-05-30T12:00:00" }
            """));

        mockStockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                { "movements": [
                  { "amount": 60, "description": "Initial", "date": "2025-05-30T12:00:00" },
                  { "amount": 40, "description": "Restock", "date": "2025-05-30T12:05:00" }
                ] }
            """));

        mockMvc.perform(get("/api/product/{id}/track", createdProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movements.length()").value(2))
                .andExpect(jsonPath("$.movements[0].description").value("Initial"))
                .andExpect(jsonPath("$.movements[1].description").value("Restock"));
    }

    @Test
    @Order(6)
    void testUpdateProduct() throws Exception {
        var update = new ProductDTO(null, "Test product-upd", "Desc-upd", 20.0);
        String payload = objectMapper.writeValueAsString(update);

        mockMvc.perform(put("/api/product/{id}", createdProductId)
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test product-upd"));
    }

    @Test
    @Order(7)
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/product/{id}", createdProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test product-upd"));


        mockMvc.perform(get("/api/product/{id}", createdProductId))
                .andExpect(status().isNotFound());
    }

}
