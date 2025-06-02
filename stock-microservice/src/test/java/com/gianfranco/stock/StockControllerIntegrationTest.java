package com.gianfranco.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gianfranco.stock.dto.stock.MovementDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StockControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("invsis")
            .withUsername("admin")
            .withPassword("admin");

    private static MockWebServer mockProductServer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // ProductClient
        registry.add("product.service.url",
                () -> mockProductServer.url("/api/product").toString()
        );
    }

    @BeforeAll
    static void startMockServer() throws Exception {
        mockProductServer = new MockWebServer();
        mockProductServer.start();
    }

    @AfterAll
    static void shutdownMockServer() throws Exception {
        mockProductServer.shutdown();
    }

    @BeforeEach
    void setupStubs() {
        mockProductServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("{ \"id\": 1, \"name\": \"ProdTest\", \"description\": \"Desc\", \"price\": 5.0 }")
        );
    }


    @Test
    @Order(1)
    void testAddMovementCreatesStock() throws Exception {
        MovementDTO movement = new MovementDTO(10L, "Initial Stock", LocalDateTime.now());
        String json = objectMapper.writeValueAsString(movement);

        mockMvc.perform(post("/api/stock/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    @Order(2)
    void testGetStockReturnsQuantity() throws Exception {
        mockMvc.perform(get("/api/stock/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    @Order(3)
    void testGetStockMovementsReturnsList() throws Exception {
        mockMvc.perform(get("/api/stock/{id}/movements", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movements").isArray())
                .andExpect(jsonPath("$.movements[0].description").value("Initial Stock"));
    }

    @Test
    @Order(4)
    void testGetAllStocksReturnsList() throws Exception {
        mockMvc.perform(get("/api/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Order(5)
    void testDeleteStockByProductId() throws Exception {
        mockMvc.perform(delete("/api/stock/{id}", 1))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/stock/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void testAddMovementWhenProductNotExistsReturnsNotFound() throws Exception {
        mockProductServer.enqueue(new MockResponse().setResponseCode(404));

        MovementDTO movement = new MovementDTO(5L, "Error - Stock", null);
        String json = objectMapper.writeValueAsString(movement);

        mockMvc.perform(post("/api/stock/{id}", 999)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

}
