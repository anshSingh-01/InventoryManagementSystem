package org.springproject.inventorymangaement.entity;
import com.hazelcast.transaction.TransactionOptions;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory_ledger")
public class InventoryLedger  extends BaseEntity{



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionOptions.TransactionType transactionType;

    @Column(name = "quantity_delta", precision = 15, scale = 4, nullable = false)
    private BigDecimal quantityDelta;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    public InventoryLedger(){
        super();
    }

    public InventoryLedger(TransactionOptions.TransactionType transactionType, BigDecimal quantityDelta, String referenceId, OffsetDateTime createdAt) {
        super(createdAt != null ? createdAt : OffsetDateTime.now(), OffsetDateTime.now());
        this.transactionType = transactionType;
        this.quantityDelta = quantityDelta;
        this.referenceId = referenceId;
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

}
