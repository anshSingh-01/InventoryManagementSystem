package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.InventoryBalance;

import java.util.UUID;

public interface InventoryBalanceRepositoryImpl extends JpaRepository<InventoryBalance, UUID> {
}
