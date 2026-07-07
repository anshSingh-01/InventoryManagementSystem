package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.compositeid.InventoryBalanceId;
import org.springproject.inventorymangaement.entity.InventoryBalance;

import java.util.Optional;
import java.util.UUID;

public interface InventoryBalanceRepositoryImpl extends JpaRepository<InventoryBalance, UUID> {


    Optional<InventoryBalance> findById(InventoryBalanceId id);

}
