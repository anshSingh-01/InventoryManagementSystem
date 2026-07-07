package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.WarehouseDto;
import org.springproject.inventorymangaement.services.WarehouseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    // Get all warehouses
    @GetMapping
    public ResponseEntity<List<WarehouseDto>> getAllWarehouses() {
        List<WarehouseDto> warehouses = warehouseService.getWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    // Get a specific warehouse by ID
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable UUID id) {
        WarehouseDto warehouseDto = warehouseService.getWarehouseById(id);
        if (warehouseDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(warehouseDto);
    }

    // Add a single warehouse
    @PostMapping
    public ResponseEntity<StatusSender> createWarehouse(@RequestBody WarehouseDto warehouseDto) {
        StatusSender response = warehouseService.addWarehouse(warehouseDto);
        return ResponseEntity.ok(response);
    }

    // Bulk add warehouses
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllWarehouses(@RequestBody List<WarehouseDto> warehouseDtos) {
        StatusSender response = warehouseService.addWarehouses(warehouseDtos);
        return ResponseEntity.ok(response);
    }
}