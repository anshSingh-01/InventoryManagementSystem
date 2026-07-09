package org.springproject.inventorymangaement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springproject.inventorymangaement.entity.BaseEntity;
import org.springproject.inventorymangaement.users.User;

@Entity
public class UserCredential extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    @NotNull
    private String password;

    public UserCredential(){

    }

    public String getEmail(){
            return user.getEmail();
    }

    public void setUser(User user){
        this.user =user;
    }

    public User getUser() {
        return this.user;
    }

    // Utility method to hash password before saving
    public void setPassword(String rawPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(rawPassword);
    }

    public boolean matchesPassword(String rawPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, this.password);
    }

}
