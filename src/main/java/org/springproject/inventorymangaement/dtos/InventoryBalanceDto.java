package org.springproject.inventorymangaement.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public class InventoryBalanceDto {


    private UUID sku_id;
    private UUID warehouse_id;
    private BigDecimal quantityOnHand;

//    @Column(name = "quantity_available", precision = 15, scale = 4, nullable = false)
    private BigDecimal quantityAvailable;

    public InventoryBalanceDto(UUID sku_id, UUID warehouse_id, BigDecimal quantityOnHand, BigDecimal quantityAvailable) {
        this.sku_id = sku_id;
        this.warehouse_id = warehouse_id;
        this.quantityOnHand = quantityOnHand;
        this.quantityAvailable = quantityAvailable;
    }

    public InventoryBalanceDto() {
    }

    public UUID getSkuId() {
        return sku_id;
    }

    public void setSkuId(UUID sku_id) {
        this.sku_id = sku_id;
    }

    public UUID getWarehouseId() {
        return warehouse_id;
    }

    public void setWarehouseId(UUID warehouse_id) {
        this.warehouse_id = warehouse_id;
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
