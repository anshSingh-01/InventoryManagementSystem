package org.springproject.inventorymangaement.dtos;

import java.time.OffsetDateTime;

public class SupplierDto {

    private String name;
    private String contactEmail;
    private Integer leadTimeDays; // Integer wrapper allows null if unknown
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public SupplierDto(){}
    public SupplierDto(String name, String contactEmail, Integer leadTimeDays, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.name = name;
        this.contactEmail = contactEmail;
        this.leadTimeDays = leadTimeDays;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Integer getLeadTimeDays() {
        return leadTimeDays;
    }

    public void setLeadTimeDays(Integer leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
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
