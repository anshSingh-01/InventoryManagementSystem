package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springproject.inventorymangaement.entity.Order;
import org.springproject.inventorymangaement.entity.OrderItem;
import org.springproject.inventorymangaement.entity.Sku;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderItemRepositoryImpl extends JpaRepository<OrderItem, UUID> {

    OrderItem findByOrderIdAndSkuId(UUID orderId , UUID skuId);

    @Query("select oi.quantity from OrderItem oi where oi.sku.id = ?1 ")
   List<BigDecimal>  findReservedOrdersByProductId(UUID sku_id);

    @Query("select oi.quantity from OrderItem oi where oi.sku.id = ?1")
    List<BigDecimal> findOrdersByProductId(UUID sku_id);

    @Query("select oi.sku.id ,oi.quantity from OrderItem oi where oi.order.id = ?1 ")
    List<Object[]> getAllSkusByOrderId(UUID order_id);



}
