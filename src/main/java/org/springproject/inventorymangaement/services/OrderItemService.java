package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.OrderItemDto;
import org.springproject.inventorymangaement.dtos.SkuDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.Order;
import org.springproject.inventorymangaement.entity.OrderItem;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.entity.Warehouse;
import org.springproject.inventorymangaement.enums.OrderStatus;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.OrderItemRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.OrderRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.ReorderRuleRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.SkuRepositoryImpl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class OrderItemService implements DtoImpl<OrderItem, OrderItemDto> {


    @Autowired
    private OrderItemRepositoryImpl orderItemRepository;

    @Autowired
    private OrderRepositoryImpl orderRepository;

    @Autowired
    private SkuRepositoryImpl skuRepository;


//    public record InventoryDto();

    // adding OrderItem
    public StatusSender addOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = DtoToEntity(orderItemDto);

        // Fail-fast validation check if relational parent mappings are completely absent
        if (orderItem.getOrder() == null || orderItem.getSku() == null) {
            return new StatusSender(StatusCode.ERROR, "Failed to save OrderItem: Invalid Order ID or Sku ID references.", orderItemDto);
        }

        orderItemRepository.save(orderItem);
        return new StatusSender(StatusCode.SUCCESS, "Added Order Item Successfully", orderItemDto);
    }


    // add order items
    public StatusSender addOrderItems(List<OrderItemDto> orderItemDtos) {
        List<OrderItem> orderItems = orderItemDtos.stream()
                .map(this::DtoToEntity)
                .filter(item -> item.getOrder() != null && item.getSku() != null) // Only persist complete components
                .toList();

        orderItemRepository.saveAll(orderItems);
        return new StatusSender(StatusCode.SUCCESS, "Added all valid order items successfully", orderItemDtos);
    }

    // get order items
    public List<OrderItemDto> getOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                .map(this::EntityToDto)
                .toList();
    }

    public Long getReservedSkus(UUID product_id) {
        return orderItemRepository.findReservedOrdersByProductId(product_id).stream()
                .mapToLong(BigDecimal::intValue).sum();
    }

    public BigDecimal getSkus(UUID product_id) {
        BigDecimal total = new BigDecimal(0);

        Long sum = orderItemRepository.findOrdersByProductId(product_id).stream()
                .mapToLong(BigDecimal::intValue).sum();
        return new BigDecimal(sum);
    }


    // get order item
    public OrderItemDto getOrderItemById(UUID id) {
        return EntityToDto(orderItemRepository.findById(id).orElse(null));
    }


    public List<Object[]> getSkusAndQuantityByOrderId(UUID order_id) {
        return orderItemRepository.getAllSkusByOrderId(order_id);
    }

    public List<OrderItem> getOrderItemByOrderRef(String orderRef) {
        return orderItemRepository.getAllOrderItemsByOrderRef(orderRef);
    }

    public void saveAllOrderItems(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }

    Comparator<OrderItem> lastUpdateDate = (d1, d2) -> d2.getUpdatedAt().compareTo(d1.getUpdatedAt());

    public List<String> getFullfilledOrderRef() {

        List<OrderItem> orderItems = orderItemRepository.findAll().stream().sorted(lastUpdateDate).toList();
        List<String> orderRefs = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {

            if (orderItem.getOrder().getStatus() == OrderStatus.FULFILLED)
                orderRefs.add(orderItem.getOrder().getOrderReference());

        }
        return orderRefs;
    }


    @Override
    public OrderItem DtoToEntity(OrderItemDto orderItemDto) {
        // Checking if this exact combo combination already exists to prevent duplicate insertion lines
        OrderItem orderItem = orderItemRepository.findByOrderIdAndSkuId(
                orderItemDto.getOrderId(),
                orderItemDto.getSkuId()
        );

        OrderItem finalizedItem = Objects.requireNonNullElseGet(orderItem, () -> new OrderItem(
                orderItemDto.getQuantity(),
                orderItemDto.getUnitPrice()
        ));

        // If it already exists, update its quantity and price fields
        if (orderItem != null) {
            finalizedItem.setQuantity(orderItemDto.getQuantity());
            finalizedItem.setUnitPrice(orderItemDto.getUnitPrice());
        }

        // 1. Fetch full Order Object from DB using the DTO's UUID
        if (orderItemDto.getOrderId() != null) {
            Optional<Order> orderOpt = orderRepository.findById(orderItemDto.getOrderId());
            orderOpt.ifPresent(finalizedItem::setOrder);
        }

        // 2. Fetch full Sku Object from DB using the DTO's UUID
        if (orderItemDto.getSkuId() != null) {
            Optional<Sku> skuOpt = skuRepository.findById(orderItemDto.getSkuId());
            skuOpt.ifPresent(finalizedItem::setSku);
        }

        return finalizedItem;
    }

    @Override
    public OrderItemDto EntityToDto(OrderItem orderItem) {
        if (orderItem == null) return null;

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setUnitPrice(orderItem.getUnitPrice());

        // Extract raw UUID references safely from the persistent entities to pass to the client DTO layer
        if (orderItem.getOrder() != null) {
            orderItemDto.setOrderId(orderItem.getOrder().getId());
        }
        if (orderItem.getSku() != null) {
            orderItemDto.setSkuId(orderItem.getSku().getId());
        }

        return orderItemDto;
    }
}