package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.entity.Product;

import java.time.OffsetDateTime;

public class ProductDto {

    private String name;
    private String brand;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public ProductDto(){}
    public ProductDto(String name, String brand, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductDto(Product product) {
        this.name = product.getName();
        this.brand = product.getBrand();
        this.description = product.getDescription();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
