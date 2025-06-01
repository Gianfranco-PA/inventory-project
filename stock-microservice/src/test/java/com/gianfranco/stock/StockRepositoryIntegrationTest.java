package com.gianfranco.stock;

import com.gianfranco.stock.model.Stock;
import com.gianfranco.stock.repository.IStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
public class StockRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("invsis")
            .withUsername("admin")
            .withPassword("admin");

    @Autowired
    private IStockRepository repository;

    @DynamicPropertySource
    static void overrideDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanRepository() {
        repository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        Stock stock = new Stock(null, 1L, 50L, LocalDateTime.now());

        Stock saved = repository.save(stock);
        Optional<Stock> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getProductId()).isEqualTo(1L);
        assertThat(found.get().getQuantity()).isEqualTo(50L);
    }

    @Test
    void testFindAll() {
        var s1 = new Stock(null, 1L, 10L, LocalDateTime.now());
        var s2 = new Stock(null, 2L, 20L, LocalDateTime.now().minusDays(1));
        repository.saveAll(List.of(s1, s2));

        List<Stock> all = StreamSupport.stream(repository.findAll().spliterator(), false).toList();

        assertThat(all).hasSize(2)
                .extracting(Stock::getProductId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testUpdateStock() {
        Stock stock = new Stock(null, 3L, 30L, LocalDateTime.now());
        Stock saved = repository.save(stock);

        saved.setQuantity(35L);
        repository.save(saved);
        Optional<Stock> updated = repository.findById(saved.getId());

        assertThat(updated).isPresent();
        assertThat(updated.get().getQuantity()).isEqualTo(35L);
    }

    @Test
    void testDeleteById() {
        Stock stock = new Stock(null, 4L, 40L, LocalDateTime.now());
        Stock saved = repository.save(stock);
        Long id = saved.getId();

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }
}
