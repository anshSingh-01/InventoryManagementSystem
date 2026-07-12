package org.springproject.inventorymangaement.services;

import com.hazelcast.transaction.TransactionOptions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springproject.inventorymangaement.dtos.*;
import org.springproject.inventorymangaement.entity.InventoryBalance;
import org.springproject.inventorymangaement.entity.InventoryLedger;
import org.springproject.inventorymangaement.entity.Order;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.enums.OrderStatus;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.enums.TransactionType;
import org.springproject.inventorymangaement.records.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

@Service
public class OrderReservationService {

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


    // changes happended
    @Transactional
    public StatusSender reservingOrder(OrderReservation orderReservation) {

        String orderReference = orderReservation.orderReference();

        if(orderService.isOrderReferencePresent(orderReference)){
            return new StatusSender(StatusCode.INVALIDINPUT,"OrderReference must have to be unique !!!",null);
        }

        UUID user_id = orderReservation.user_id();

        List<Items> items = orderReservation.items();
        orderService.addOrder(new OrderDto(orderReference, OrderStatus.RESERVED));
        UUID order_id = orderService.getId(orderReference);
        BigDecimal quantity = orderReservation.quantity();
        for (Items items1 : items) {

            UUID sku_id = items1.sku_id();
//                            BigDecimal quantity = items1.quantity();

            // checking if the skuItems are enough

            List<Object[]> lists = inventoryBalanceService.getListOfWarehouseBySkuId(sku_id);

//                                lists.stream()
//                                        .forEach((list)->System.out.println(""+(UUID)list[0] +" ,"+(BigDecimal)(list[1])));

            int totalAvailable = 0;

            for (Object[] obj : lists) {
                totalAvailable += ((BigDecimal) (obj[1])).intValue();
            }

            if (totalAvailable >= quantity.intValue()) {


                for (int i = 0; i < lists.size() && quantity.intValue() != 0; i++) {


                    Object[] list = lists.get(i);

                    UUID warehouse_id = (UUID) list[0];
                    BigDecimal quantityAvailable = (BigDecimal) list[1];

                    if (quantityAvailable.intValue() < quantity.intValue()) {

                        StatusSender status = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, (BigDecimal) list[1], orderReference + "{RESERVED}", OffsetDateTime.now()));
                        InventoryLedger inventoryLedger = (InventoryLedger) status.getObj();
                        inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), user_id));
                        list[1] = new BigDecimal(0);
                        quantity = quantity.subtract(quantityAvailable);

                    } else {
                        StatusSender status = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, quantity, orderReference + "{RESERVED}", OffsetDateTime.now()));
                        list[1] = quantityAvailable.subtract(quantity);
                        InventoryLedger inventoryLedger = (InventoryLedger) status.getObj();
                        inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), user_id));
                        quantity = new BigDecimal(0);
                        i = lists.size();
                    }

                    if (quantity.intValue() == 0) i = lists.size();
                }

                inventoryBalanceService.deleteZeroQuantityAvailableRows();

                int flag = 0;
                for (Object[] list : lists) {

                    if (flag == 1) break;
                    UUID warehouse_id = (UUID) list[0];
                    BigDecimal q = (BigDecimal) list[1];


                    InventoryBalance inventoryBalance = inventoryBalanceService.getInstanceOfIB(sku_id, warehouse_id);

                    if (inventoryBalance.getQuantityAvailable().intValue() > q.intValue()) {
                        // made this mistake
                        // BigDecimal quantityOnHand = inventoryBalance.getQuantityOnHand().subtract(inventoryBalance.getQuantityAvailable().subtract(q));
                        inventoryBalance.setQuantityAvailable(q);

                        //   inventoryBalance.setQuantityOnHand(quantityOnHand);

                        inventoryBalanceService.editIB(inventoryBalance);
                        flag = 1;
                    }


                }

                orderItemService.addOrderItem(new OrderItemDto(order_id, sku_id, items1.quantity(), items1.unitPrice()));
//                                lists.stream()
//                                        .forEach((list)->System.out.println(""+(UUID)list[0] +" ,"+(BigDecimal)(list[1])));

            } else {

                return new StatusSender(StatusCode.INVALIDINPUT,"Insufficient balance!!!",null);
            }
        }



        return new StatusSender(StatusCode.SUCCESS, "OrderPlaced", null);
    }



    // Reorder service

    public StatusSender stockReorderService(StockReorderDetails stockReorderDetails){

                UUID warehouse_id = stockReorderDetails.warehouse_id();
                UUID supplier_id = stockReorderDetails.supplier_id();
                String reference_id = stockReorderDetails.reference_id();

                List<Items>items = stockReorderDetails.items();

                items.stream()
                        .forEach(

                                (item)->{
                                    UUID sku_id = item.sku_id();
                                    BigDecimal quantity = item.quantity();
                                    BigDecimal unitPrice = item.unitPrice();

                                    InventoryBalance inventoryBalance =inventoryBalanceService.getInstanceOfIB(sku_id ,warehouse_id);

                                    inventoryBalance.setQuantityOnHand(inventoryBalance.getQuantityOnHand().add(quantity));
                                    inventoryBalance.setQuantityAvailable(inventoryBalance.getQuantityAvailable().add(quantity));

                                    inventoryBalanceService.editIB(inventoryBalance);

                                    StatusSender statusSender= inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id,warehouse_id, TransactionOptions.TransactionType.ONE_PHASE,quantity,reference_id + "{RECEIVED}",OffsetDateTime.now()));
                                    inventoryActionService.addAction(new InventoryActionDto(((InventoryLedger)(statusSender.getObj())).getId() , supplier_id));

                                }


                        );

                return new StatusSender(StatusCode.SUCCESS ,"reorder Successfull",items);

    }

    // get the details of stock

    public List<ReorderAlert> getAllReorderAlert(){

                return inventoryBalanceService.getReorderStock().stream()
                        .map(

                                (obj) ->{
                                                UUID sku_id = (UUID)(obj[0]);
                                                UUID warehouse_id = (UUID)(obj[1]);
                                                BigDecimal quantity = (BigDecimal) obj[2];

                                                return new ReorderAlert(sku_id,warehouse_id,quantity);
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
                           StatusSender statusSender = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, delta, orderReference +"{CANCELLED", OffsetDateTime.now()));
                            inventoryActionService.addAction(new InventoryActionDto( ((InventoryLedger)(statusSender.getObj())).getId(),user_id));;
                            inventoryBalanceService.editIB(inventoryBalance);

                        }

                );

        return new StatusSender(StatusCode.SUCCESS, "OrderCancelled", null);
    }

    @Transactional
    public void fullfillmentDispatch(Queue<String> queue){

             while(!queue.isEmpty()){

                        String orderReference = queue.peek();
                        queue.remove();




             }

    }



    @Transactional
    public StatusSender fullfillmentDispatch(FullfillmentDispatch fullfillmentDispatch, UUID user_id) {

        String orderReference = fullfillmentDispatch.orderReference();
        UUID warehouse_id = fullfillmentDispatch.warehouse_id();
        String trackingNumber = fullfillmentDispatch.trackingNumber();


        UUID order_id = orderService.getId(orderReference);

        List<Object[]> lists = orderItemService.getSkusAndQuantityByOrderId(order_id);

        for (Object[] list : lists) {

            UUID sku_id = (UUID) list[0];
            BigDecimal quantity = (BigDecimal) list[1];

             InventoryBalance inventoryBalance =inventoryBalanceService.getInstanceOfIB(sku_id,warehouse_id);

             inventoryBalance.setQuantityOnHand(inventoryBalance.getQuantityOnHand().subtract(quantity));
             inventoryBalanceService.editIB(inventoryBalance);

            StatusSender statusSender= inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, quantity, "Fulfill Order: " + orderReference + " (Tracking: " + trackingNumber + ")" + "{RELEASED}", OffsetDateTime.now()));

            inventoryActionService.addAction(new InventoryActionDto( ((InventoryLedger)(statusSender.getObj())).getId(),user_id));
        }
        Order order = orderService.getOrder(orderReference);
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

                            (obj)->{

                                    UUID sku_id = (UUID)(obj[0]);
                                    Sku sku = skuService.findById(sku_id);
                                    BigDecimal quantity = (BigDecimal)(obj[1]);

                                    return new ItemLog(sku.getId() , sku.getSkuCode(),sku.getProduct().getName(),quantity);

                            }

                    ).toList());


    }
//
//
//
}
