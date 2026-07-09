package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springproject.inventorymangaement.entity.UserCredential;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepoImpl extends JpaRepository<UserCredential, UUID> {

        Optional<UserCredential>  findByUserEmail(String email);

}
