package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.entity.BaseEntity;
import org.springproject.inventorymangaement.entity.Product;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ProductDto extends BaseEntity {


    private String name;
    private String brand;
    private String description;


    public ProductDto(){
        super();
    }


    public ProductDto(String name, String brand, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(createdAt,updatedAt);
        this.name = name;
        this.brand = brand;
        this.description = description;

    }

    public ProductDto(Product product) {
        super();
        if (product != null) {
            this.setId(product.getId());
            this.setCreatedAt(product.getCreatedAt());
            this.setUpdatedAt(product.getUpdatedAt());
            this.name = product.getName();
            this.brand = product.getBrand();
            this.description = product.getDescription();
        }
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


}
