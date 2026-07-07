package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.ReorderRule;

import java.util.UUID;

public interface ReorderRuleRepositoryImpl extends JpaRepository<ReorderRule, UUID> {

        ReorderRule findBySkuIdAndWarehouseId(UUID skuId ,UUID warehouseId);

}
