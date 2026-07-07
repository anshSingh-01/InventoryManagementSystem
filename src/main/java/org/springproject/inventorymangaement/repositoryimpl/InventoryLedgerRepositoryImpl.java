package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.InventoryLedger;

import java.util.UUID;

public interface InventoryLedgerRepositoryImpl extends JpaRepository<InventoryLedger, UUID> {
}
