package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.Warehouse;

import java.util.UUID;

public interface WarehouseRepositoryImpl extends JpaRepository<Warehouse, UUID> {

        Warehouse findByNameAndAddress(String name ,String address);


}
