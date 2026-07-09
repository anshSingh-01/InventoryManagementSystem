package org.springproject.inventorymangaement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class OrderItem  extends BaseEntity{

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;
    private BigDecimal quantity;  // Supports ordering fractions (e.g., 2.5 kg of raw materials)
    private BigDecimal unitPrice; // Enforces monetary accuracy

    public OrderItem(){
        super();
    }

    public OrderItem(Order order, Sku sku, BigDecimal quantity, BigDecimal unitPrice) {

        this.order = order;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderItem(BigDecimal quantity, BigDecimal unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
