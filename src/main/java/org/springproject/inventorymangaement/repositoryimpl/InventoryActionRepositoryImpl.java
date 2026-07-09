package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.InventoryAction;

import java.util.Optional;
import java.util.UUID;

public interface InventoryActionRepositoryImpl extends JpaRepository<InventoryAction, UUID> {


    Optional<InventoryAction> findByUserEmail(String email);
}
