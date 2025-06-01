package com.gianfranco.products;

import com.gianfranco.products.model.Product;
import com.gianfranco.products.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@DataJpaTest
public class ProductRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("invsis")
            .withUsername("admin")
            .withPassword("admin");

    @Autowired
    private IProductRepository repository;

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
        Product product = new Product(null, "SaveTest", "Test save and find", 9.99);

        Product saved = repository.save(product);
        Optional<Product> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("SaveTest");
        assertThat(found.get().getPrice()).isEqualTo(9.99);
    }

    @Test
    void testFindAll() {
        var p1 = new Product(null, "A", "Desc A", 1.0);
        var p2 = new Product(null, "B", "Desc B", 2.0);
        repository.saveAll(List.of(p1, p2));

        List<Product> all = StreamSupport.stream(repository.findAll().spliterator(), false).toList();

        assertThat(all).hasSize(2)
                .extracting(Product::getName)
                .containsExactlyInAnyOrder("A", "B");
    }

    @Test
    void testUpdateProduct() {
        Product product = repository.save(new Product(null, "UpTest", "Before update", 5.0));

        product.setName("UpTest-Updated");
        product.setPrice(6.0);
        repository.save(product);
        Optional<Product> updated = repository.findById(product.getId());

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("UpTest-Updated");
        assertThat(updated.get().getPrice()).isEqualTo(6.0);
    }

    @Test
    void testDeleteById() {
        Product product = repository.save(new Product(null, "DelTest", "Test deleteById", 4.0));
        Long id = product.getId();

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }

}
