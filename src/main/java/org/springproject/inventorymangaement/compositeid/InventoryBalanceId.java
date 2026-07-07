package org.springproject.inventorymangaement.compositeid;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class InventoryBalanceId implements Serializable {

    private UUID skuId;
    private UUID warehouseId;

    public InventoryBalanceId() {}

    public InventoryBalanceId(UUID skuId, UUID warehouseId) {
        this.skuId = skuId;
        this.warehouseId = warehouseId;
    }

    public UUID getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    // Hibernate requires accurate equals() and hashCode() implementations for composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryBalanceId that = (InventoryBalanceId) o;
        return Objects.equals(skuId, that.skuId) && Objects.equals(warehouseId, that.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuId, warehouseId);
    }
}
