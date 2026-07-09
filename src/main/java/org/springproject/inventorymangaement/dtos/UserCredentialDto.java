package org.springproject.inventorymangaement.dtos;

//import org.springproject.inventorymangaement.entity.UserCredential;

import org.springproject.inventorymangaement.entity.BaseEntity;

public class UserCredentialDto extends BaseEntity {

    private String email;
    private String password;

    UserCredentialDto(){

    }

    public UserCredentialDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
