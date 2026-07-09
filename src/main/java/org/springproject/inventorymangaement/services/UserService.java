package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.UserDto;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.UserRepositoryImpl;
import org.springproject.inventorymangaement.users.User;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements DtoImpl<User, UserDto> {

    @Autowired
    UserRepositoryImpl userRepository;


    public StatusSender addUser(UserDto userDto){
                User user = DtoToEntity(userDto);
                userRepository.save(user);
                return new StatusSender(StatusCode.SUCCESS,"User Created Successfull",userDto);
    }

    public StatusSender addUsers(List<UserDto> userDtos){
        List<User> users = userDtos.stream()
                        .map(this::DtoToEntity).toList();
        userRepository.saveAll(users);
        return new StatusSender(StatusCode.SUCCESS,"User Created Successfull",userDtos);
    }

    public List<User> getUsers(){
            List<User> users = userRepository.findAll();
            return users;
    }

    public User getUserByEmail(String email){
            return userRepository.findByEmail(email);
    }

    public User getUserById(UUID id){
            return userRepository.findById(id).get();
    }


    @Override
    public User DtoToEntity(UserDto userDto) {
            User user = userRepository.findByEmail(userDto.getEmail());
            if(user == null){
                    return new User(userDto.getName(),userDto.getEmail(),userDto.getRole());
            }
            return user;
    }

    @Override
    public UserDto EntityToDto(User user) {
            return new UserDto(user.getCreatedAt(),user.getUpdatedAt(),user.getName(),user.getEmail(),user.getRole());
    }

}
