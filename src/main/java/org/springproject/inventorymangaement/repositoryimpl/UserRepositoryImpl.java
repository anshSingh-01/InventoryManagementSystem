package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.users.User;

import java.util.UUID;

public interface UserRepositoryImpl extends JpaRepository<User, UUID> {

        User findByEmail(String email);

}
