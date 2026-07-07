package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.entity.Order;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemDto {


    private UUID order_id;


    private UUID sku_id;
    private BigDecimal quantity;  // Supports ordering fractions (e.g., 2.5 kg of raw materials)
    private BigDecimal unitPrice; // Enforces monetary accuracy


    public UUID getOrderId() {
        return order_id;
    }

    public void setOrderId(UUID order_id) {
        this.order_id = order_id;
    }

    public UUID getSkuId() {
        return sku_id;
    }

    public void setSkuId(UUID sku_id) {
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
