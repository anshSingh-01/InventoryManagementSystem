package org.springproject.inventorymangaement.entity;

import jakarta.persistence.*;
import org.springproject.inventorymangaement.compositeid.InventoryBalanceId;

import java.math.BigDecimal;

@Entity
@Table(name = "inventory_balances")
public class InventoryBalance {

    @EmbeddedId             // it is used to  create composite key
    private InventoryBalanceId id = new InventoryBalanceId();


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skuId")
    @JoinColumn(name = "sku_id")
    private Sku sku;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "quantity_on_hand", precision = 15, scale = 4, nullable = false)
    private BigDecimal quantityOnHand;

    @Column(name = "quantity_available", precision = 15, scale = 4, nullable = false)
    private BigDecimal quantityAvailable;

    // Getters, Setters, Constructors

    public InventoryBalance(){}


    public InventoryBalance(BigDecimal quantityOnHand, BigDecimal quantityAvailable) {
        this.quantityOnHand = quantityOnHand;
        this.quantityAvailable = quantityAvailable;
    }

    public InventoryBalanceId getId() {
        return id;
    }

    public void setId(InventoryBalanceId id) {
        this.id = id;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(BigDecimal quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public BigDecimal getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(BigDecimal quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
}
