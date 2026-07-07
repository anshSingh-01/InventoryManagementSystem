package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.SupplierDto;
import org.springproject.inventorymangaement.services.SupplierService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // Get all suppliers
    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        List<SupplierDto> suppliers = supplierService.getSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    // Get a specific supplier by ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable UUID id) {
        try {
            SupplierDto supplierDto = supplierService.getSupplierById(id);
            return ResponseEntity.ok(supplierDto);
        } catch (Exception e) {
            // Catches cases where .get() fails on an empty Optional in the service layer
            return ResponseEntity.notFound().build();
        }
    }

    // Add a single supplier
    @PostMapping
    public ResponseEntity<StatusSender> createSupplier(@RequestBody SupplierDto supplierDto) {
        StatusSender response = supplierService.addSupplier(supplierDto);
        return ResponseEntity.ok(response);
    }

    // Bulk add suppliers
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllSuppliers(@RequestBody List<SupplierDto> supplierDtos) {
        StatusSender response = supplierService.addSupliers(supplierDtos);
        return ResponseEntity.ok(response);
    }
}