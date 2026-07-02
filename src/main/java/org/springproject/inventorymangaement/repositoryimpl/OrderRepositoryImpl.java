package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.Order;

import java.util.UUID;

public interface OrderRepositoryImpl extends JpaRepository<Order, UUID> {
}
