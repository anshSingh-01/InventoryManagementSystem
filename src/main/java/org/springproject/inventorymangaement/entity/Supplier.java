package org.springproject.inventorymangaement.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;
@Entity
public class Supplier extends BaseEntity {

    private String name;
    private String contactEmail;
    private Integer leadTimeDays; // Integer wrapper allows null if unknown


    public Supplier(){
        super();
    }

    public Supplier( String name, String contactEmail, Integer leadTimeDays) {

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
