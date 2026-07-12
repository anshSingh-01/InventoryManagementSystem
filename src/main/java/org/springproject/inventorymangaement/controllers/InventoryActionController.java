package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.InventoryActionDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.InventoryAction;
import org.springproject.inventorymangaement.services.InventoryActionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory-actions")
public class InventoryActionController {

    @Autowired
    InventoryActionService inventoryActionService;

    @GetMapping
    public List<InventoryAction> getActions(){
        return inventoryActionService.getActions();
    }

    @GetMapping("/{email}")
    public Optional<InventoryAction> getActionsByEmail(@PathVariable String email){
            return inventoryActionService.getActionsByEmail(email);
    }

    @PostMapping
    public ResponseEntity<StatusSender> addInventoryAction(@RequestBody InventoryActionDto inventoryActionDto){
                return ResponseEntity.ok(inventoryActionService.addAction(inventoryActionDto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> addInventoryActions(@RequestBody List<InventoryAction> inventoryActions){
        return ResponseEntity.ok(inventoryActionService.addActions(inventoryActions));
    }


}
