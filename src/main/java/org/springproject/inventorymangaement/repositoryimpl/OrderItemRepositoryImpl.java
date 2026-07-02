package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.OrderItem;

import java.util.UUID;

public interface OrderItemRepositoryImpl extends JpaRepository<OrderItem, UUID> {
}
