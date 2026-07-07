package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.OrderDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.Order;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.OrderRepositoryImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService implements DtoImpl<Order, OrderDto> {

    @Autowired
    private OrderRepositoryImpl orderRepository;

    // adding Order
    public StatusSender addOrder(OrderDto orderDto) {
        Order order = DtoToEntity(orderDto);
        orderRepository.save(order);
        // Map back the generated ID or ensure it is returned
        orderDto.setId(order.getId());
        return new StatusSender(StatusCode.SUCCESS, "Added Order Successfully", orderDto);
    }

    // add orders
    public StatusSender addOrders(List<OrderDto> orderDtos) {
        List<Order> orders = orderDtos.stream()
                .map(this::DtoToEntity)
                .toList();
        orderRepository.saveAll(orders);
        // Map back IDs for bulk response
        for (int i = 0; i < orders.size(); i++) {
            orderDtos.get(i).setId(orders.get(i).getId());
        }
        return new StatusSender(StatusCode.SUCCESS, "Added all orders successfully", orderDtos);
    }

    // get orders
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::EntityToDto)
                .toList();
    }

    // get order
    public OrderDto getOrderById(UUID id) {
        return EntityToDto(orderRepository.findById(id).orElse(null));
    }

    @Override
    public Order DtoToEntity(OrderDto orderDto) {
        if (orderDto.getId() != null) {
            Optional<Order> existing = orderRepository.findById(orderDto.getId());
            if (existing.isPresent()) {
                Order o = existing.get();
                o.setOrderReference(orderDto.getOrderReference());
                o.setStatus(orderDto.getStatus());
                return o;
            }
        }
        // Enforcing idempotency: check if the unique order reference already exists in the database
        Order order = orderRepository.findByOrderReference(orderDto.getOrderReference());

        return Objects.requireNonNullElseGet(order, () -> new Order(
                orderDto.getOrderReference(),
                orderDto.getStatus(),
                orderDto.getCreatedAt(),
                orderDto.getUpdatedAt()
        ));
    }

    @Override
    public OrderDto EntityToDto(Order order) {
        if (order == null) return null;

        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderReference(order.getOrderReference());
        orderDto.setStatus(order.getStatus());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setUpdatedAt(order.getUpdatedAt());

        return orderDto;
    }
}