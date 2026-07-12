package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.SkuDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.services.SkuService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skus")
public class SkuController {

    @Autowired
    private SkuService skuService;

    // Get all SKUs
    @GetMapping
    public ResponseEntity<List<SkuDto>> getAllSkus() {
        List<SkuDto> skus = skuService.findAllSkus();
        return ResponseEntity.ok(skus);
    }

    // Get a specific SKU by ID
    @GetMapping("/{id}")
    public ResponseEntity<SkuDto> getSkuById(@PathVariable UUID id) {
        SkuDto skuDto = skuService.EntityToDto(skuService.findById(id));
        if (skuDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skuDto);
    }

    // Add a single SKU
    @PostMapping
    public ResponseEntity<StatusSender> createSku(@RequestBody SkuDto skuDto) {
        StatusSender response = skuService.addSku(skuDto);
        return ResponseEntity.ok(response);
    }

    // Bulk add SKUs
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllSkus(@RequestBody List<SkuDto> skuDtos) {
        StatusSender response = skuService.addAllSkus(skuDtos);
        return ResponseEntity.ok(response);
    }

    // Edit an existing SKU
    @PutMapping("/{id}")
    public ResponseEntity<StatusSender> updateSku(@PathVariable UUID id, @RequestBody SkuDto skuDto) {
        StatusSender response = skuService.editSku(id, skuDto);
        return ResponseEntity.ok(response);
    }

    // Delete a SKU by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusSender> deleteSkuById(@PathVariable UUID id) {
        StatusSender response = skuService.DeleteById(id);
        return ResponseEntity.ok(response);
    }
}