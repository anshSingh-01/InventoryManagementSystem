package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springproject.inventorymangaement.entity.InventoryAction;

import java.util.Optional;
import java.util.UUID;

public interface InventoryActionRepositoryImpl extends JpaRepository<InventoryAction, UUID> {


    @Query("select ia from InventoryAction ia where ia.inventoryLedger.id =?1 and ia.user.id=?2")
    Optional<InventoryAction> findByUserIdAndLogId(UUID user_id , UUID log_id);

    Optional<InventoryAction> findByUserEmail(String email);
}
