package org.springproject.inventorymangaement.dtos;

import com.hazelcast.transaction.TransactionOptions;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class InventoryLedgerDto {


    private UUID sku_id;

    private UUID warehouse_id;

    private TransactionOptions.TransactionType transactionType;

    private BigDecimal quantityDelta;

    private String referenceId;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    public InventoryLedgerDto(){}

    public InventoryLedgerDto(UUID sku_id, UUID warehouse_id, TransactionOptions.TransactionType transactionType, BigDecimal quantityDelta, String referenceId, OffsetDateTime createdAt) {
        this.sku_id = sku_id;
        this.warehouse_id = warehouse_id;
        this.transactionType = transactionType;
        this.quantityDelta = quantityDelta;
        this.referenceId = referenceId;
        this.createdAt = createdAt;
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

    public TransactionOptions.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionOptions.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getQuantityDelta() {
        return quantityDelta;
    }

    public void setQuantityDelta(BigDecimal quantityDelta) {
        this.quantityDelta = quantityDelta;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


