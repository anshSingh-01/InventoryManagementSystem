package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.OrderItemDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.services.OrderItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    // Get all order items
    @GetMapping
    public ResponseEntity<List<OrderItemDto>> getAllOrderItems() {
        List<OrderItemDto> items = orderItemService.getOrderItems();
        return ResponseEntity.ok(items);
    }

    // Get a specific order item by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable UUID id) {
        OrderItemDto itemDto = orderItemService.getOrderItemById(id);
        if (itemDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemDto);
    }

    // Add a single order item
    @PostMapping
    public ResponseEntity<StatusSender> createOrderItem(@RequestBody OrderItemDto orderItemDto) {
        StatusSender response = orderItemService.addOrderItem(orderItemDto);
        if (response.getStatusCode() == StatusCode.ERROR) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Bulk add order items
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllOrderItems(@RequestBody List<OrderItemDto> orderItemDtos) {
        StatusSender response = orderItemService.addOrderItems(orderItemDtos);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/checkout")
//    public ResponseEntity<StatusSender> checkoutDemo(@RequestBody Sku sku){
//
//
//
//    }




}