package org.springproject.inventorymangaement.dtos;
import org.springproject.inventorymangaement.entity.BaseEntity;
import org.springproject.inventorymangaement.enums.Roles;

import java.time.OffsetDateTime;

public class UserDto extends BaseEntity {

    private String name;
    private String email;
    private Roles role;

    public UserDto(){}
    public UserDto(String name, String email, Roles role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }


    public UserDto(OffsetDateTime createdAt, OffsetDateTime updatedAt, String name, String email, Roles role) {
        super(createdAt, updatedAt);
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
