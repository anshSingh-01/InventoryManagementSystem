package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.entity.BaseEntity;
import org.springproject.inventorymangaement.enums.OrderStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public class OrderDto extends BaseEntity {


    private String orderReference;
    private OrderStatus status;


    public OrderDto(){
        super();
    }

    public OrderDto( String orderReference, OrderStatus status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {

        super(createdAt,updatedAt);
        this.orderReference = orderReference;
        this.status = status;

    }

    public OrderDto(String orderReference, OrderStatus status) {
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
