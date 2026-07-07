package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.ProductDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.services.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    // Add a single product
    @PostMapping
    public ResponseEntity<StatusSender> createProduct(@RequestBody ProductDto productDto) {
        StatusSender response = productService.addProduct(productDto);
        return ResponseEntity.ok(response);
    }

    // Bulk add products (batch)
    @PostMapping("/batchs")
    public ResponseEntity<StatusSender> createAllProducts(@RequestBody List<ProductDto> productDtos) {
        StatusSender response = productService.addAllProducts(productDtos);
        return ResponseEntity.ok(response);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusSender> deleteProductById(@PathVariable UUID id) {
        StatusSender response = productService.DeleteById(id);
        return ResponseEntity.ok(response);
    }
}