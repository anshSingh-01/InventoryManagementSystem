package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springproject.inventorymangaement.entity.Order;

import java.util.List;
import java.util.Queue;
import java.util.UUID;

public interface OrderRepositoryImpl extends JpaRepository<Order, UUID> {

        Order findByOrderReference(String orderRefrence);

        @Query("select o.orderReference from Order o where o.status=RESERVED order by o.createdAt asc")
        Queue<String> getOrderReferences();

}
