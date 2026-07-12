package org.springproject.inventorymangaement.controllers;

import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.records.RelocationTransaction;
import org.springproject.inventorymangaement.services.RelocationService;

@RestController
@RequestMapping("/api/relocation")
public class RelocationController {

    @Autowired
    RelocationService relocationService;
    @PostMapping("/items")

    public ResponseEntity<StatusSender> relocationItem(@RequestBody RelocationTransaction relocationTransaction){
            return ResponseEntity.ok(relocationService.relocatingItems(relocationTransaction));
    }




}
