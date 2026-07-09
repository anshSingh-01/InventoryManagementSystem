package org.springproject.inventorymangaement.users;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springproject.inventorymangaement.entity.BaseEntity;
import org.springproject.inventorymangaement.enums.Roles;

import java.util.UUID;
@Entity
@Table(name = "users")
public class User extends BaseEntity {


        @NotBlank
        private String name;

       @Column(nullable = false, unique = true)
        private String email;

        @Enumerated(EnumType.STRING)
        private Roles role;

        public User(){
            super();
        }

    public User( String name,String email, Roles role) {
        this.name = name;
        this.role = role;
        this.email =email;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Roles getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
