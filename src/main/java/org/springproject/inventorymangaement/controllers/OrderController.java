package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.OrderDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.services.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Get all orders
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    // Get a specific order by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        OrderDto orderDto = orderService.getOrderById(id);
        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDto);
    }

    // Add a single order
    @PostMapping
    public ResponseEntity<StatusSender> createOrder(@RequestBody OrderDto orderDto) {
        StatusSender response = orderService.addOrder(orderDto);
        return ResponseEntity.ok(response);
    }

    // Bulk add orders
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllOrders(@RequestBody List<OrderDto> orderDtos) {
        StatusSender response = orderService.addOrders(orderDtos);
        return ResponseEntity.ok(response);
    }
}