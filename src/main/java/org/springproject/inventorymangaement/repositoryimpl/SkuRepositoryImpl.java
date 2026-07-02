package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.Sku;

import java.util.UUID;

public interface SkuRepositoryImpl extends JpaRepository<Sku, UUID> {

    Sku findBySkuCode(String id);

}
