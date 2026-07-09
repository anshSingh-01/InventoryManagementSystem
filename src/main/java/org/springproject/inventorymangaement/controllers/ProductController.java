package org.springproject.inventorymangaement.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.ProductDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.Product;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.services.ProductService;

import java.util.Collection;
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
    public ResponseEntity<StatusSender> createProduct(@Valid @RequestBody ProductDto productDto) {
//       if(!check(productDto)){
//            return ResponseEntity.badRequest().body(new StatusSender(StatusCode.INVALIDINPUT,"INVALID INPUT",null));
//       }
        StatusSender response = productService.addProduct(productDto);
        return ResponseEntity.ok(response);
    }

    // Bulk add products (batch)
    @PostMapping("/batchs")
    public ResponseEntity<StatusSender> createAllProducts(@Valid @RequestBody List<ProductDto> productDtos) {
//        if(!check(productDtos)){
//            return ResponseEntity.badRequest().body(new StatusSender(StatusCode.INVALIDINPUT,"INVALID INPUT",null));
//        }
        StatusSender response = productService.addAllProducts(productDtos);
        return ResponseEntity.ok(response);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusSender> deleteProductById(@PathVariable UUID id) {
        StatusSender response = productService.DeleteById(id);
        return ResponseEntity.ok(response);
    }



    // validation for products
//    boolean check(ProductDto productDto) {
//        if (productDto == null) {
//            return false;
//        }
//
//        // Check name
//        if (productDto.getName() == null || productDto.getName().isBlank()) {
//            return false;
//        }
//
//        // Check brand
//        if (productDto.getBrand() == null || productDto.getBrand().isBlank()) {
//            return false;
//        }
//
//        // Check description
//        if (productDto.getDescription() == null || productDto.getDescription().isBlank()) {
//            return false;
//        }
//
//        return true;
//    }
//
//    boolean check( List<ProductDto> productDtos){
//
//        for(ProductDto productDto : productDtos){
//                if(!check(productDto))return false;
//        }
//        return true;
//    }

}