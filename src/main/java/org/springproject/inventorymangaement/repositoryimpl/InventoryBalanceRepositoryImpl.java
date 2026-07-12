package org.springproject.inventorymangaement.repositoryimpl;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springproject.inventorymangaement.compositeid.InventoryBalanceId;
import org.springproject.inventorymangaement.entity.InventoryBalance;
import org.springproject.inventorymangaement.entity.Warehouse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface InventoryBalanceRepositoryImpl extends JpaRepository<InventoryBalance, UUID> {


    @Query("Select w.warehouse from InventoryBalance w where w.sku.id = ?1")
    List<Warehouse> findWarehouseBySkuId(UUID sku_id);


    @Query("select ib.sku.skuCode , ib.sku.product.name,ib.quantityAvailable from InventoryBalance ib where ib.warehouse.id =?1")
    List<Object[]> getAllSkusInWarehouse(UUID warehouse_id);

    @Query("Select COUNT(w.sku) from InventoryBalance w where w.sku.id = ?1")
    long countSkuBySkuId(UUID sku_id);

    @Query("Select w.warehouse.id , w.quantityAvailable from InventoryBalance w where w.sku.id = ?1  ORDER BY w.quantityAvailable desc")
    List<Object[]>grpByWarehouseAndCountSkus(UUID sku_id);

    Optional<InventoryBalance> findById(InventoryBalanceId id);
    @Query("select ib from InventoryBalance ib where ib.sku.id=?1 and ib.warehouse.id =?2")
    Optional<InventoryBalance> findById(UUID sku_id , UUID warehouse);

    // this was unexpected !!!
    @Modifying
    @Transactional
    @Query("delete  from InventoryBalance b where b.sku.id=?1 and b.warehouse.id=?2")
    void deleteColumnByWarehouseIdAndSkuId(UUID w_id , UUID sku_id);

    @Modifying
    @Transactional

    @Query("delete from InventoryBalance ib where ib.quantityAvailable = 0 and ib.quantityOnHand = 0")
    void deleteZeroQuantityAvailableRows();

    @Query("select ib.quantityAvailable from InventoryBalance ib where ib.sku.id=?1 and ib.warehouse.id =?2")
    BigDecimal findQuantityAvailable(UUID sku_id ,UUID warehouse_id);

    @Query("select ib.quantityOnHand from InventoryBalance ib where ib.sku.id=?1 and ib.warehouse.id =?2")
    BigDecimal findQuantityOnHand(UUID sku_id ,UUID warehouse_id);

    @Query("select ib.sku.id ,ib.warehouse.id, ib.quantityAvailable from InventoryBalance ib where ib.quantityAvailable < 5")
    List<Object[]> getAllReorderStock();


}
