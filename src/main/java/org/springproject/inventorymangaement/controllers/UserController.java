package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.UserDto;
import org.springproject.inventorymangaement.services.UserService;
import org.springproject.inventorymangaement.users.User;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getUsers(){
            return userService.getUsers();
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email){
            return userService.getUserByEmail(email);
    }

    @PostMapping
    public ResponseEntity<StatusSender> addUser(@RequestBody UserDto userDto){
            return ResponseEntity.ok(userService.addUser(userDto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> addUsers(@RequestBody List<UserDto> userDtos){
        return ResponseEntity.ok(userService.addUsers(userDtos));
    }

}
