package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.entity.BaseEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class SupplierDto extends BaseEntity {


    private String name;
    private String contactEmail;
    private Integer leadTimeDays; // Integer wrapper allows null if unknown


    public SupplierDto(){
        super();
    }
    public SupplierDto( String name, String contactEmail, Integer leadTimeDays, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(createdAt,updatedAt);
        this.name = name;
        this.contactEmail = contactEmail;
        this.leadTimeDays = leadTimeDays;

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

}
