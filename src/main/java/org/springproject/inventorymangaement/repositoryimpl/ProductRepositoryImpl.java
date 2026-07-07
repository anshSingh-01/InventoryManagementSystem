package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.Product;

import java.util.UUID;

public interface ProductRepositoryImpl extends JpaRepository<Product, UUID> {

        Product findByNameAndBrand(String name , String brand);

}
