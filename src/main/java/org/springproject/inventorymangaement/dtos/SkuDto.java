package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.entity.BaseEntity;
import tools.jackson.core.ObjectReadContext;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.prefs.BackingStoreException;

public class SkuDto extends BaseEntity {

    private String skuCode;

    private UUID product_id;

    private BigDecimal weight;

    // Getters, Setters, Constructors

    public SkuDto(){
        super();
    }

    public SkuDto(String skuCode, BigDecimal weight) {
        super();
        this.skuCode = skuCode;
        this.weight = weight;
    }

    public SkuDto(String skuCode, UUID product_id,BigDecimal weight) {
        super();
        this.skuCode = skuCode;
        this.weight = weight;
        this.product_id =product_id;
    }

    public UUID getProduct_id() {
        return product_id;
    }

    public void setProduct_id(UUID product_id) {
        this.product_id = product_id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

}
