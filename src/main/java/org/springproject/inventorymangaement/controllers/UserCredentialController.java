package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.UserCredentialDto;
import org.springproject.inventorymangaement.entity.UserCredential;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.services.UserCredentialService;

@RestController
@RequestMapping("/api")
public class UserCredentialController {

    @Autowired
    UserCredentialService userCredentialService;

    @PostMapping("/signin")
    public ResponseEntity<StatusSender> signIn(@RequestBody UserCredentialDto userCredentialDto){

        StatusSender statusSender =userCredentialService.SignIn(userCredentialDto);

        if(statusSender.getStatusCode()!= StatusCode.SUCCESS)return ResponseEntity.badRequest().body(statusSender);

        return ResponseEntity.ok((statusSender));

    }


    @PostMapping("/login")
    public ResponseEntity<StatusSender> login(@RequestBody UserCredentialDto userCredentialDto){

        StatusSender statusSender =userCredentialService.Login(userCredentialDto);

        if(statusSender.getStatusCode()!= StatusCode.SUCCESS)return ResponseEntity.badRequest().body(statusSender);

        return ResponseEntity.ok((statusSender));

    }


}
