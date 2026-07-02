package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.enums.WarehouseType;

import java.time.OffsetDateTime;

public class WarehouseDto {

    private String name;
    private WarehouseType type; // Enforced compile-time safety
    private String address;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public WarehouseDto(){}
    public WarehouseDto(String name, WarehouseType type, String address, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
