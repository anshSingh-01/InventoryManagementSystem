package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springproject.inventorymangaement.entity.InventoryLedger;

import java.util.List;
import java.util.UUID;

public interface InventoryLedgerRepositoryImpl extends JpaRepository<InventoryLedger, UUID> {

            @Query("select il from InventoryLedger il where il.sku.id=?1 and il.warehouse.id=?2")
            InventoryLedger findBYTwoIds(UUID sku_id , UUID warehouse_id);

    @Query("select il from InventoryLedger il where il.sku.id=?1 and il.warehouse.id=?2 order by il.createdAt desc")
            List<InventoryLedger> findInventoryLedgerByOrderReference(String orderReference);


}
