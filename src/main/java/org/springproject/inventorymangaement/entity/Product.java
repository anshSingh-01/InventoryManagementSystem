package org.springproject.inventorymangaement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class Product extends BaseEntity{

    @NotNull
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotBlank
    private String description;


    public Product(){
        super();
    }

    public Product( String name, String brand, String description) {
        this.name = name;
        this.brand = brand;
        this.description = description;
    }

    public Product(String name, String brand, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {

        super(createdAt,updatedAt);
        this.name = name;
        this.brand = brand;
        this.description = description;

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
