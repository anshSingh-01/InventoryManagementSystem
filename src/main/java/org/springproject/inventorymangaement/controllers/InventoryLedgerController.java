package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.InventoryLedgerDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.services.InventoryLedgerService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory-ledgers")

public class InventoryLedgerController {

    @Autowired
    private InventoryLedgerService inventoryLedgerService;

    // Get all ledger entries
    @GetMapping

    public ResponseEntity<List<InventoryLedgerDto>> getAllLedgerEntries() {
        List<InventoryLedgerDto> entries = inventoryLedgerService.getLedgerEntries();
        return ResponseEntity.ok(entries);
    }

    // Get a specific ledger entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<InventoryLedgerDto> getLedgerEntryById(@PathVariable UUID id) {
        InventoryLedgerDto ledgerDto = inventoryLedgerService.getLedgerEntryById(id);
        if (ledgerDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ledgerDto);
    }

    // Add a single ledger entry
    @PostMapping
    public ResponseEntity<StatusSender> createLedgerEntry(@RequestBody InventoryLedgerDto inventoryLedgerDto) {
        StatusSender response = inventoryLedgerService.addLedgerEntry(inventoryLedgerDto);
        if (response.getStatusCode() == StatusCode.ERROR) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Bulk add ledger entries
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllLedgerEntries(@RequestBody List<InventoryLedgerDto> inventoryLedgerDtos) {
        StatusSender response = inventoryLedgerService.addLedgerEntries(inventoryLedgerDtos);
        return ResponseEntity.ok(response);
    }
}