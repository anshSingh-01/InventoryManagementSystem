package org.springproject.inventorymangaement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Discount {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        UUID id;

        @OneToOne(cascade = CascadeType.PERSIST)
        Sku sku;

        BigDecimal discount;

        LocalDate startTime;
        LocalDate endTime;

        boolean active;

        public Discount(){}

    public Discount(Sku sku, BigDecimal discount, LocalDate startTime, LocalDate endTime, boolean active) {
        this.sku = sku;
        this.discount = discount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
