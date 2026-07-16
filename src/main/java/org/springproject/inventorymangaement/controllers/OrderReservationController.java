package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.records.*;
import org.springproject.inventorymangaement.services.OrderReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderReservationController {

    @Autowired
    OrderReservationService orderReservationService;



    @PostMapping("/reserve-order")
    public ResponseEntity<StatusSender> reserveOrder(@RequestBody OrderReservation orderReservation) {
        StatusSender statusSender = orderReservationService.reservingOrder(orderReservation);

        if(statusSender.getStatusCode() == StatusCode.INVALIDINPUT){
            return ResponseEntity.badRequest().body(statusSender);
        }

        return ResponseEntity.ok(statusSender);
    }

    @PostMapping("/order-cancel/{user_id}/{orderReference}")
    public ResponseEntity<StatusSender> CancelOrder(@PathVariable("user_id") UUID user_id, @PathVariable("orderReference")  String orderReference) {

        StatusSender statusSender = orderReservationService.CancellingOrder(user_id, orderReference);
        return ResponseEntity.ok(statusSender);
    }

    @PostMapping("/order-fullfill/{user_id}")
    public ResponseEntity<StatusSender> fullfillmentOrder(@RequestBody FullfillmentDispatch fullfillmentDispatch, @PathVariable UUID user_id) {

        StatusSender statusSender = orderReservationService.fullfillmentDispatch(fullfillmentDispatch,user_id);
        return ResponseEntity.ok(statusSender);
    }


    @PostMapping("/cancel-reorder") // it can take either one or list of reorder cancel....
    public ResponseEntity<StatusSender> cancelingReorderStock(@RequestBody StockReorderDetails stockReorderDetails) {
        StatusSender statusSender = orderReservationService.stockReorderCanceling(stockReorderDetails);
        return ResponseEntity.ok(statusSender);
    }

    @PostMapping("/reorder-items")
    public ResponseEntity<StatusSender> reorderStock(@RequestBody StockReorderDetails stockReorderDetails) {

        StatusSender statusSender = orderReservationService.stockReorderService(stockReorderDetails);
        return ResponseEntity.ok(statusSender);
    }

    @GetMapping("/reorder-alert")
    public List<ReorderAlert> reorderAlertService(){return orderReservationService.getAllReorderAlert();}


    @GetMapping("/bill-details/{orderReference}")
    public BillRecord getBillDetails(@PathVariable String orderReference){return orderReservationService.generateBill(orderReference);}

    @GetMapping("/reservation-lookup/{order_reference}")
    public ReservationLookup reservationLookup(String order_reference) {return orderReservationService.getReservationLookup(order_reference);}
}
