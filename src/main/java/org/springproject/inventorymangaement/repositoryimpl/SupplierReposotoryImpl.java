package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.Supplier;

import java.util.UUID;

public interface SupplierReposotoryImpl extends JpaRepository<Supplier, UUID> {

            Supplier findByNameAndContactEmail(String name, String contactEmail);

}
