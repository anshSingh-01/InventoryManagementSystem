package org.springproject.inventorymangaement.services;

import com.hazelcast.transaction.TransactionOptions;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.*;
import org.springproject.inventorymangaement.entity.*;
import org.springproject.inventorymangaement.enums.OrderStatus;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.records.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OrderReservationService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    @Autowired
    InventoryBalanceService inventoryBalanceService;

    @Autowired
    SkuService skuService;

    @Autowired
    InventoryLedgerService inventoryLedgerService;

    @Autowired
    InventoryActionService inventoryActionService;

    @Autowired
    DiscountService discountService;

    // direct fulling system
    public void startFullfillingOrder() {
//        logger.info("Fulfillment scheduler is started");
//        scheduler.scheduleAtFixedRate(() -> {
//            Queue<String> activeQueue = orderService.getOrderReference();
//            if (activeQueue != null && !activeQueue.isEmpty()) {
//                fullfillmentDispatch(activeQueue);
//            }
//        }, 0, 2, TimeUnit.MINUTES);
    }

    // changes happended
    @Transactional
    public StatusSender reservingOrder(OrderReservation orderReservation) {

        String orderReference = orderReservation.orderReference();

        if (orderService.isOrderReferencePresent(orderReference)) {
            return new StatusSender(StatusCode.INVALIDINPUT, "OrderReference must have to be unique !!!", null);
        }

        UUID user_id = orderReservation.user_id();

        List<Items> items = orderReservation.items();
        orderService.addOrder(new OrderDto(orderReference, OrderStatus.RESERVED));
        UUID order_id = orderService.getId(orderReference);

        for (Items items1 : items) {

            UUID sku_id = items1.sku_id();
            BigDecimal skuQuantity = items1.quantity();
            if (skuQuantity == null) {
                skuQuantity = orderReservation.quantity();
            }

            // checking if the skuItems are enough
            List<Object[]> lists = inventoryBalanceService.getListOfWarehouseBySkuId(sku_id);

            int totalAvailable = 0;
            for (Object[] obj : lists) {
                totalAvailable += ((BigDecimal) (obj[1])).intValue();
            }

            if (totalAvailable >= skuQuantity.intValue()) {

                for (int i = 0; i < lists.size() && skuQuantity.intValue() != 0; i++) {

                    Object[] list = lists.get(i);

                    UUID warehouse_id = (UUID) list[0];
                    BigDecimal quantityAvailable = (BigDecimal) list[1];

                    if (quantityAvailable.intValue() < skuQuantity.intValue()) {

                        StatusSender status = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, (BigDecimal) list[1], orderReference + "{RESERVE}", OffsetDateTime.now()));
                        InventoryLedger inventoryLedger = (InventoryLedger) status.getObj();
                        inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), user_id));
                        list[1] = new BigDecimal(0);
                        skuQuantity = skuQuantity.subtract(quantityAvailable);

                    } else {
                        StatusSender status = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, skuQuantity, orderReference + "{RESERVE}", OffsetDateTime.now()));
                        list[1] = quantityAvailable.subtract(skuQuantity);
                        InventoryLedger inventoryLedger = (InventoryLedger) status.getObj();
                        inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), user_id));
                        skuQuantity = new BigDecimal(0);
                        i = lists.size();
                    }

                    if (skuQuantity.intValue() == 0) i = lists.size();
                }

                inventoryBalanceService.deleteZeroQuantityAvailableRows();

                for (Object[] list : lists) {
                    UUID warehouse_id = (UUID) list[0];
                    BigDecimal q = (BigDecimal) list[1];

                    InventoryBalance inventoryBalance = inventoryBalanceService.getInstanceOfIB(sku_id, warehouse_id);
                    if (inventoryBalance != null && inventoryBalance.getQuantityAvailable().compareTo(q) != 0) {
                        inventoryBalance.setQuantityAvailable(q);
                        inventoryBalanceService.editIB(inventoryBalance);
                    }
                }
                BigDecimal price = new BigDecimal(new Random().nextDouble(900) + 100);
                orderItemService.addOrderItem(new OrderItemDto(order_id, sku_id, items1.quantity() != null ? items1.quantity() : orderReservation.quantity(), price.setScale(2, RoundingMode.HALF_UP)));

            } else {

                return new StatusSender(StatusCode.INVALIDINPUT, "Insufficient balance!!!", null);
            }
        }


        return new StatusSender(StatusCode.SUCCESS, "OrderPlaced", null);
    }

    // Reorder service
    public StatusSender stockReorderService(StockReorderDetails stockReorderDetails) {

        UUID warehouse_id = stockReorderDetails.warehouse_id();
        UUID supplier_id = stockReorderDetails.supplier_id();
        String reference_id = stockReorderDetails.reference_id();

        List<Items> items = stockReorderDetails.items();

        items.stream()
                .forEach(

                        (item) -> {
                            UUID sku_id = item.sku_id();
                            BigDecimal quantity = item.quantity();
                            BigDecimal unitPrice = item.unitPrice();

                            InventoryBalance inventoryBalance = inventoryBalanceService.getInstanceOfIB(sku_id, warehouse_id);

                            inventoryBalance.setQuantityOnHand(inventoryBalance.getQuantityOnHand().add(quantity));
                            inventoryBalance.setQuantityAvailable(inventoryBalance.getQuantityAvailable().add(quantity));

                            inventoryBalanceService.editIB(inventoryBalance);

                            StatusSender statusSender = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.ONE_PHASE, quantity, reference_id + "{RECEIVED}", OffsetDateTime.now()));
                            inventoryActionService.addAction(new InventoryActionDto(((InventoryLedger) (statusSender.getObj())).getId(), supplier_id));

                        }


                );

        return new StatusSender(StatusCode.SUCCESS, "reorder Successfull", items);

    }

    // stock canceling reorder
    public StatusSender stockReorderCanceling(StockReorderDetails stockReorderDetails) {


        UUID warehouse_id = stockReorderDetails.warehouse_id();
        UUID supplier_id = stockReorderDetails.supplier_id();
        String reference_id = stockReorderDetails.reference_id();

        List<Items> itemsList = stockReorderDetails.items();

        for (Items items : itemsList) {


//            if(items.quantity().intValue()  > 0)continue;
            StatusSender statusSender = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(items.sku_id(), warehouse_id, TransactionOptions.TransactionType.ONE_PHASE, items.quantity(), reference_id + "{CANCEL}", OffsetDateTime.now()));

            InventoryLedger inventoryLedger = (InventoryLedger) statusSender.getObj();

            inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), supplier_id));

            inventoryBalanceService.deleteByTwoIds(warehouse_id, items.sku_id());

        }

        return new StatusSender(StatusCode.SUCCESS, "SuccessFully Removed all reoderItems", null);
    }

    // get the details of stock
    public List<ReorderAlert> getAllReorderAlert() {

        return inventoryBalanceService.getReorderStock().stream()
                .map(

                        (obj) -> {
                            UUID sku_id = (UUID) (obj[0]);
                            UUID warehouse_id = (UUID) (obj[1]);
                            BigDecimal quantity = (BigDecimal) obj[2];

                            return new ReorderAlert(sku_id, warehouse_id, quantity);
                        }

                ).toList();

    }

    public StatusSender CancellingOrder(UUID user_id, String orderReference) {

        // 1)  find order
        Order order = orderService.getOrder(orderReference);
        order.setStatus(OrderStatus.CANCELLED);
        orderService.editOrder(order);
        List<InventoryLedger> inventoryLedgers = inventoryLedgerService.getListOfLedgers(orderReference);

        inventoryLedgers.stream()
                .forEach(

                        inventoryLedger -> {

                            BigDecimal delta = inventoryLedger.getQuantityDelta();

                            UUID sku_id = inventoryLedger.getSku().getId();
                            UUID warehouse_id = inventoryLedger.getWarehouse().getId();

                            InventoryBalance inventoryBalance = inventoryBalanceService.getInstanceOfIB(sku_id, warehouse_id);

                            inventoryBalance.setQuantityAvailable(inventoryBalance.getQuantityAvailable().add((delta.abs())));
//                            inventoryBalance.setQuantityOnHand(inventoryBalance.getQuantityOnHand().add(delta.abs()));
                            StatusSender statusSender = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, delta, orderReference + "{RECIEVE}", OffsetDateTime.now()));
                            inventoryActionService.addAction(new InventoryActionDto(((InventoryLedger) (statusSender.getObj())).getId(), user_id));

                            inventoryBalanceService.editIB(inventoryBalance);

                        }

                );

        return new StatusSender(StatusCode.SUCCESS, "OrderCancelled", null);
    }

    // tracking id
    static String generateTrackingId() {
        Random random = new Random();
        int number = 1000 + random.nextInt(9000); // ensures 4 digits (1000–9999)

        System.out.println("TRACK-" + number);

        return "TRACK-" + number;
    }

    @Transactional
    public void fullfillmentDispatch(Queue<String> queue) {
        String trackingNumber = generateTrackingId();
        UUID systemUserId = UUID.fromString("12140365-6e10-4955-8bd7-06cb9041b8b5");

        while (!queue.isEmpty()) {
            String orderReference = queue.poll();
            if (orderReference == null) continue;

            Order order = orderService.getOrder(orderReference);
            if (order == null || order.getStatus() == OrderStatus.FULFILLED || order.getStatus() == OrderStatus.CANCELLED) {
                continue;
            }

            List<InventoryLedger> inventoryLedgers = inventoryLedgerService.getListOfLedgers(orderReference);

            for (InventoryLedger ledger : inventoryLedgers) {
                if (!ledger.getReferenceId().contains("RESERVE")) {
                    continue;
                }

                UUID sku_id = ledger.getSku().getId();
                UUID warehouse_id = ledger.getWarehouse().getId();
                BigDecimal quantity = ledger.getQuantityDelta().abs();

                InventoryBalance inventoryBalance = inventoryBalanceService.getInstanceOfIB(sku_id, warehouse_id);
                if (inventoryBalance != null) {
                    inventoryBalance.setQuantityOnHand(inventoryBalance.getQuantityOnHand().subtract(quantity));
                    inventoryBalanceService.editIB(inventoryBalance);
                }

                StatusSender statusSender = inventoryLedgerService.addLedgerEntry(
                        new InventoryLedgerDto(
                                sku_id,
                                warehouse_id,
                                TransactionOptions.TransactionType.TWO_PHASE,
                                quantity,
                                "Fulfill Order: " + orderReference + " (Tracking: " + trackingNumber + ")" + "{RELEASE}",
                                OffsetDateTime.now()
                        )
                );

                inventoryActionService.addAction(new InventoryActionDto(((InventoryLedger) (statusSender.getObj())).getId(), systemUserId));
            }

            order.setStatus(OrderStatus.FULFILLED);
            orderService.editOrder(order);
        }
    }

    // Generate bill everytime any order fullfill

    @Transactional
    public BillRecord generateBill(String orderReference) {

        List<OrderItem> orderItems = orderItemService.getOrderItemByOrderRef(orderReference);
        Order order = orderService.getOrder(orderReference);

        BigDecimal totalBill = new BigDecimal(0);

        Map<UUID, BigDecimal> discounts = discountService.getDiscountMap();

        List<BillItems> billItems = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {

            double discount = 0.0;
            Sku sku = orderItem.getSku();
            UUID sku_id = sku.getId();

            if (discounts.containsKey(sku_id)) {
                discount = discounts.get(sku_id).doubleValue() / 100.0;
            }

            String productName = sku.getProduct().getName();
            String skuName = sku.getSkuCode();
            BigDecimal quantity = orderItem.getQuantity();
            BigDecimal originalPrice = orderItem.getUnitPrice().multiply(quantity);
            BigDecimal price = originalPrice.subtract(originalPrice.multiply(BigDecimal.valueOf(discount)));
            totalBill = totalBill.add(price);

            billItems.add(new BillItems(productName, skuName, quantity, price, new BigDecimal(discount)));
        }


        return new BillRecord(orderReference, billItems, totalBill, order.getUpdatedAt());
    }


    @Transactional
    public StatusSender fullfillmentDispatch(FullfillmentDispatch fullfillmentDispatch, UUID user_id) {
        String orderReference = fullfillmentDispatch.orderReference();
        String trackingNumber = fullfillmentDispatch.trackingNumber();

        Order order = orderService.getOrder(orderReference);
        if (order == null || order.getStatus() == OrderStatus.FULFILLED || order.getStatus() == OrderStatus.CANCELLED) {
            return new StatusSender(StatusCode.ERROR, "Order is already fulfilled or cancelled", null);
        }

        List<InventoryLedger> inventoryLedgers = inventoryLedgerService.getListOfLedgers(orderReference);

        for (InventoryLedger ledger : inventoryLedgers) {
            if (!ledger.getReferenceId().contains("RESERVE")) {
                continue;
            }

            UUID sku_id = ledger.getSku().getId();
            UUID warehouse_id = ledger.getWarehouse().getId();
            BigDecimal quantity = ledger.getQuantityDelta().abs();

            InventoryBalance inventoryBalance = inventoryBalanceService.getInstanceOfIB(sku_id, warehouse_id);
            if (inventoryBalance != null) {
                inventoryBalance.setQuantityOnHand(inventoryBalance.getQuantityOnHand().subtract(quantity));
                inventoryBalanceService.editIB(inventoryBalance);
            }

            StatusSender statusSender = inventoryLedgerService.addLedgerEntry(
                    new InventoryLedgerDto(
                            sku_id,
                            warehouse_id,
                            TransactionOptions.TransactionType.TWO_PHASE,
                            quantity,
                            "Fulfill Order: " + orderReference + " (Tracking: " + trackingNumber + ")" + "{RELEASE}",
                            OffsetDateTime.now()
                    )
            );

            inventoryActionService.addAction(new InventoryActionDto(((InventoryLedger) (statusSender.getObj())).getId(), user_id));
        }

        order.setStatus(OrderStatus.FULFILLED);
        orderService.editOrder(order);

        return new StatusSender(StatusCode.SUCCESS, "Updated ledger Successfully", null);
    }

    // fixed this reservation lookup service
    @Transactional
    public ReservationLookup getReservationLookup(String orderReference) {

        Order order = orderService.getOrder(orderReference);

        return new ReservationLookup(orderReference, orderItemService.getSkusAndQuantityByOrderId(order.getId()).stream()
                .map(

                        (obj) -> {

                            UUID sku_id = (UUID) (obj[0]);
                            Sku sku = skuService.findById(sku_id);
                            BigDecimal quantity = (BigDecimal) (obj[1]);

                            return new ItemLog(sku.getId(), sku.getSkuCode(), sku.getProduct().getName(), quantity);

                        }

                ).toList());


    }

}
