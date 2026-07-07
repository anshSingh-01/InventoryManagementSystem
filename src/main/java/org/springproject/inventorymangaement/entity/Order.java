package org.springproject.inventorymangaement.entity;

import jakarta.persistence.*;
import org.springproject.inventorymangaement.enums.OrderStatus;

import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private String orderReference;
    private OrderStatus status;

    public Order(){
        super();
    }
    public Order( String orderReference, OrderStatus status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.orderReference = orderReference;
        this.status = status;
    }


    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}