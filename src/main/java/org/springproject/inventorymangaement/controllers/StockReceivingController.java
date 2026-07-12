package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.records.AuditAdjustment;
import org.springproject.inventorymangaement.records.ReceivingTransaction;
import org.springproject.inventorymangaement.records.SkuDetailsPerWarehosue;
import org.springproject.inventorymangaement.services.StockReceivingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
public class StockReceivingController {

    @Autowired
    StockReceivingService stockReceivingService;

    @PostMapping("/add-balance")
    public ResponseEntity<StatusSender> addStockBalance(@RequestBody ReceivingTransaction receivingTransaction) {
            StatusSender statusSender =stockReceivingService.addBalanceInInventory(receivingTransaction);

            if(statusSender.getStatusCode() == StatusCode.INVALIDINPUT){
                return ResponseEntity.badRequest().body(statusSender);
            }

        return ResponseEntity.ok(statusSender);

    }

    @PostMapping("/manager-audit")
    public ResponseEntity<StatusSender> managerAuditCorrection(@RequestBody AuditAdjustment auditAdjustment) {

        return ResponseEntity.ok(stockReceivingService.managerAuditCorrection(auditAdjustment));

    }

    @GetMapping("/skus-per-warehouse/{warehouse_id}")
    public List<SkuDetailsPerWarehosue> managerAuditCorrection(@PathVariable UUID warehouse_id) {

        return stockReceivingService.getSkuDetailPerWarehouse(warehouse_id);

    }


}
