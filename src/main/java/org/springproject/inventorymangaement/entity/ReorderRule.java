package org.springproject.inventorymangaement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "reorder_rules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reorder_rules_sku_warehouse",
                        columnNames = {"sku_id", "warehouse_id"}
                )
        }
)
public class ReorderRule extends BaseEntity{

    // Direct object relation to the target SKU
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sku_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_reorder_rules_sku")
    )
    private Sku sku;

    // Direct object relation to the specific Warehouse
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "warehouse_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_reorder_rules_warehouse")
    )
    private Warehouse warehouse;

    // The safety stock floor count that triggers a replenishment event
    @Column(name = "reorder_threshold", precision = 15, scale = 4, nullable = false)
    private BigDecimal reorderThreshold;

    // The economic order quantity (EOQ) to buy when the threshold is breached
    @Column(name = "target_reorder_quantity", precision = 15, scale = 4, nullable = false)
    private BigDecimal targetReorderQuantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();



    // Getters, Setters, and Constructors

    public ReorderRule(){
        super();
    }
    public ReorderRule( BigDecimal reorderThreshold, BigDecimal targetReorderQuantity) {

        this.reorderThreshold = reorderThreshold;
        this.targetReorderQuantity = targetReorderQuantity;

    }

    public ReorderRule(BigDecimal reorderThreshold, BigDecimal targetReorderQuantity, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
            super(createdAt,updatedAt);
        this.reorderThreshold = reorderThreshold;
        this.targetReorderQuantity = targetReorderQuantity;

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

}