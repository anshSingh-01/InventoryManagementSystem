package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springproject.inventorymangaement.compositeid.InventoryBalanceId;
import org.springproject.inventorymangaement.entity.InventoryBalance;
import org.springproject.inventorymangaement.entity.Warehouse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface InventoryBalanceRepositoryImpl extends JpaRepository<InventoryBalance, UUID> {


    @Query("Select w.warehouse from InventoryBalance w where w.sku.id = ?1")
    List<Warehouse> findWarehouseBySkuId(UUID sku_id);

    @Query("Select COUNT(w.sku) from InventoryBalance w where w.sku.id = ?1")
    long countSkuBySkuId(UUID sku_id);

    @Query("Select COUNT(w.sku) from InventoryBalance w where w.sku.id = ?1 group by w.warehouse.id ORDER BY COUNT(w.sku) desc")
    List<Object[]>grpByWarehouseAndCountSkus(UUID sku_id);

    Optional<InventoryBalance> findById(InventoryBalanceId id);

    @Query("delete  from InventoryBalance b where b.sku.id=?1 and b.warehouse.id=?2")
    void deleteColumnByWarehouseIdAndSkuId(UUID w_id , UUID sku_id);

}
