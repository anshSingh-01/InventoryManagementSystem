package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.entity.BaseEntity;
import org.springproject.inventorymangaement.enums.WarehouseType;

import java.time.OffsetDateTime;
import java.util.UUID;

public class WarehouseDto extends BaseEntity {


    private String name;
    private WarehouseType type; // Enforced compile-time safety
    private String address;


    public WarehouseDto(){
        super();
    }


    public WarehouseDto(String name, WarehouseType type, String address, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(createdAt,updatedAt);
        this.name = name;
        this.type = type;
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WarehouseType getType() {
        return type;
    }

    public void setType(WarehouseType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
