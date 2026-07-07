package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.compositeid.InventoryBalanceId;
import org.springproject.inventorymangaement.dtos.InventoryBalanceDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.services.InventoryBalanceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory-balances")
public class InventoryBalanceController {

    @Autowired
    private InventoryBalanceService inventoryBalanceService;

    // Get all inventory balances
    @GetMapping
    public ResponseEntity<List<InventoryBalanceDto>> getAllInventoryBalances() {
        List<InventoryBalanceDto> balances = inventoryBalanceService.getInventoryBalances();
        return ResponseEntity.ok(balances);
    }

    // Get a specific inventory balance using composite path components
    @GetMapping("/find")
    public ResponseEntity<InventoryBalanceDto> getInventoryBalanceById(
            @RequestParam UUID skuId,
            @RequestParam UUID warehouseId) {

        InventoryBalanceId id = new InventoryBalanceId(skuId, warehouseId);
        InventoryBalanceDto balanceDto = inventoryBalanceService.getInventoryBalanceById(id);

        if (balanceDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(balanceDto);
    }

    // Add or update a single inventory balance entry
    @PostMapping
    public ResponseEntity<StatusSender> createInventoryBalance(@RequestBody InventoryBalanceDto inventoryBalanceDto) {
        StatusSender response = inventoryBalanceService.addInventoryBalance(inventoryBalanceDto);
        if (response.getStatusCode() == StatusCode.ERROR) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Bulk add or update inventory balances
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllInventoryBalances(@RequestBody List<InventoryBalanceDto> inventoryBalanceDtos) {
        StatusSender response = inventoryBalanceService.addInventoryBalances(inventoryBalanceDtos);
        return ResponseEntity.ok(response);
    }
}