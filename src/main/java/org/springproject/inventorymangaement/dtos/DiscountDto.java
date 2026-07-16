package org.springproject.inventorymangaement.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DiscountDto {

    UUID sku_id;
    BigDecimal discount;
    LocalDate startDate;
    LocalDate endDate;
    boolean active;

    public DiscountDto() {
    }

    public DiscountDto(UUID sku_id, BigDecimal discount, LocalDate startDate, LocalDate endDate, boolean active) {
        this.sku_id = sku_id;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public UUID getSku_id() {
        return sku_id;
    }

    public void setSku_id(UUID sku_id) {
        this.sku_id = sku_id;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
