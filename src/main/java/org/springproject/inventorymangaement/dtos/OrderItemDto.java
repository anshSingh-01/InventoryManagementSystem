package org.springproject.inventorymangaement.dtos;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springproject.inventorymangaement.entity.Order;
import org.springproject.inventorymangaement.entity.Sku;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemDto {


    private Order order;


    private UUID sku_id;
    private BigDecimal quantity;  // Supports ordering fractions (e.g., 2.5 kg of raw materials)
    private BigDecimal unitPrice; // Enforces monetary accuracy

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public UUID getSku_id() {
        return sku_id;
    }

    public void setSku_id(UUID sku_id) {
        this.sku_id = sku_id;
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
