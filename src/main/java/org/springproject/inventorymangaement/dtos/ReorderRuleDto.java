package org.springproject.inventorymangaement.dtos;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ReorderRuleDto {

    private UUID sku_id;
    private UUID warehouse_id;
    private BigDecimal reorderThreshold;
    private BigDecimal targetReorderQuantity;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public ReorderRuleDto(){}
    public ReorderRuleDto(UUID sku_id, UUID warehouse_id, BigDecimal reorderThreshold, BigDecimal targetReorderQuantity) {
        this.sku_id = sku_id;
        this.warehouse_id = warehouse_id;
        this.reorderThreshold = reorderThreshold;
        this.targetReorderQuantity = targetReorderQuantity;
    }

    public ReorderRuleDto(UUID sku_id, UUID warehouse_id, BigDecimal reorderThreshold, BigDecimal targetReorderQuantity, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.sku_id = sku_id;
        this.warehouse_id = warehouse_id;
        this.reorderThreshold = reorderThreshold;
        this.targetReorderQuantity = targetReorderQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getSku_id() {
        return sku_id;
    }

    public void setSku_id(UUID sku_id) {
        this.sku_id = sku_id;
    }

    public UUID getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(UUID warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public BigDecimal getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(BigDecimal reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    public BigDecimal getTargetReorderQuantity() {
        return targetReorderQuantity;
    }

    public void setTargetReorderQuantity(BigDecimal targetReorderQuantity) {
        this.targetReorderQuantity = targetReorderQuantity;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
