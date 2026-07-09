package org.springproject.inventorymangaement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity

public class Sku extends BaseEntity {


    @Column(name = "sku_code", nullable = false, unique = true)
    private String skuCode;

    // Direct Object Relationship instead of raw UUID productId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_skus_product"))
    private Product product;

    @Column(precision = 10, scale = 4)
    private BigDecimal weight;

    // Getters, Setters, Constructors

    public Sku() {
        super();
    }

    public Sku(UUID id, String skuCode, Product product, BigDecimal weight) {

        this.skuCode = skuCode;
        this.product = product;
        this.weight = weight;
    }

    public Sku(String skuCode, BigDecimal weight) {
        this.skuCode = skuCode;
        this.weight = weight;
    }


    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
