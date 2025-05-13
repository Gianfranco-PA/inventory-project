package com.gianfranco.stock.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movement> movements = new ArrayList<>();

    private Long quantity;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public Stock() {
    }

    public Stock(Long id, Long productId, List<Movement> movements, Long quantity, LocalDateTime lastUpdate) {
        this.id = id;
        this.productId = productId;
        this.movements = movements;
        this.quantity = quantity;
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    //TODO: This needs to be read-only
    public List<Movement> getMovements() {
        return movements;
    }

    //TODO: Check if necessary, it could break the quantity field.
    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    //Movements methods
    public void addMovement(Movement movement) {
        this.movements.add(movement);
        this.quantity += movement.getAmount();
    }

    public void removeMovement(Movement movement) {
        this.movements.remove(movement);
        this.quantity -= movement.getAmount();
    }
}
