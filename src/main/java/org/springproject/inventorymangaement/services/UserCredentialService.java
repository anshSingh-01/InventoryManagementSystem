package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.UserCredentialDto;
import org.springproject.inventorymangaement.entity.UserCredential;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.UserCredentialRepoImpl;
import org.springproject.inventorymangaement.users.User;

@Service
public class UserCredentialService implements DtoImpl<UserCredential, UserCredentialDto> {


    @Autowired
    UserCredentialRepoImpl userCredentialRepo;
    @Autowired
    UserService userService;


    public StatusSender SignIn(UserCredentialDto userCredentialDto) {
        userCredentialRepo.save(DtoToEntity(userCredentialDto));
        return new StatusSender(StatusCode.SUCCESS, "added credentials", null);
    }

    public StatusSender Login(UserCredentialDto userCredentialDto) {

        java.util.Optional<UserCredential> userCredentialOpt = userCredentialRepo.findByUserEmail(userCredentialDto.getEmail());
        if (userCredentialOpt.isEmpty() || !userCredentialOpt.get().matchesPassword(userCredentialDto.getPassword()))
            return new StatusSender(StatusCode.NOTFOUND, "User Don't Exist", null);
        return new StatusSender(StatusCode.SUCCESS, "you are Logged in!!!", userCredentialOpt.get().getUser());
    }


    @Override
    public UserCredential DtoToEntity(UserCredentialDto userCredentialDto) {

        UserCredential userCredential = new UserCredential();
        User user = userService.getUserByEmail(userCredentialDto.getEmail());
        userCredential.setUser(user);
        userCredential.setPassword(userCredentialDto.getPassword());
        return userCredential;
    }

    @Override
    public UserCredentialDto EntityToDto(UserCredential userCredential) {
        return null;
    }


}
