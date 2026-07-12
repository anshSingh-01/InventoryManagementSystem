package org.springproject.inventorymangaement.services;

//import com.hazelcast.transaction.TransactionOptions;
//import jakarta.transaction.Transactional;

import com.hazelcast.transaction.TransactionOptions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.*;
import org.springproject.inventorymangaement.entity.InventoryBalance;
import org.springproject.inventorymangaement.entity.InventoryLedger;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.entity.Warehouse;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.records.AuditAdjustment;
import org.springproject.inventorymangaement.records.Items;
import org.springproject.inventorymangaement.records.ReceivingTransaction;
import org.springproject.inventorymangaement.records.SkuDetailsPerWarehosue;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StockReceivingService {


    @Autowired
    InventoryBalanceService inventoryBalanceService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    UserService userService;

    @Autowired
    SkuService service;

    @Autowired
    InventoryLedgerService inventoryLedgerService;

    @Autowired
    InventoryActionService inventoryActionService;

    @Transactional
    public StatusSender addBalanceInInventory(ReceivingTransaction receivingTransaction) {

        //1)  adding it into inventory balance

        UUID warehouse_id = receivingTransaction.warehouse_id();
        UUID supplier_id = receivingTransaction.supplier_id();
        String refrence_id = receivingTransaction.refrence_id();
        List<Items> items = receivingTransaction.items();


        for (Items item : items) {


            UUID sku_id = item.sku_id();

            BigDecimal quantity = item.quantity();

            if (quantity.intValue() < 0 || service.checkMaxQuantityWarehosueTake(sku_id, quantity, null)) {
                return new StatusSender(StatusCode.INVALIDINPUT, "INVALID INPUT TRY AGAIN", null);

            }


            inventoryBalanceService.editInventoryBalance(new InventoryBalanceDto(sku_id, warehouse_id, quantity, quantity));

            StatusSender statusSender = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.TWO_PHASE, quantity, refrence_id + "{RECIEVE}", OffsetDateTime.now()));

            InventoryLedger inventoryLedger = (InventoryLedger) statusSender.getObj();
            UUID ledger_id = inventoryLedger.getId();

            inventoryActionService.addAction(new InventoryActionDto(ledger_id, supplier_id));
        }


        // updating ledgers and User audits

        return new StatusSender(StatusCode.SUCCESS, "Added All orders", null);
    }


    @Transactional
    public StatusSender managerAuditCorrection(AuditAdjustment auditAdjustment) {

        UUID sku_id = auditAdjustment.sku_id();
        UUID user_id = auditAdjustment.user_id();
        UUID warehouse_id = auditAdjustment.warehouse_id();
        BigDecimal adjustQuantity = auditAdjustment.adjustQuantity();
        String reason = auditAdjustment.reason();
        InventoryBalance inventoryBalance = inventoryBalanceService.getInstanceOfIB(sku_id, warehouse_id);


        if (inventoryBalance == null) {
            return new StatusSender(StatusCode.ERROR, " Item Not Present", null);
        }

        BigDecimal quantity = inventoryBalance.getQuantityAvailable();
        quantity = quantity.add(adjustQuantity);

        if (quantity.intValue() < 0) {
            return new StatusSender(StatusCode.INVALIDINPUT, "insufficient removal", null);
        }

        inventoryBalance.setQuantityAvailable(quantity);
        inventoryBalance.setQuantityOnHand(inventoryBalance.getQuantityOnHand().add(adjustQuantity));
        inventoryBalanceService.editIB(inventoryBalance);
        String status = adjustQuantity.intValue() < 0 ? "{RELEASE}" : "{RECIEVE}";
        StatusSender statusSender = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id, warehouse_id, TransactionOptions.TransactionType.ONE_PHASE, adjustQuantity, reason + status, OffsetDateTime.now()));

        InventoryLedger inventoryLedger = (InventoryLedger) (statusSender.getObj());
        inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), user_id));
        return new StatusSender(StatusCode.SUCCESS, "Saved Edit Successfully", inventoryBalance);
    }


    @Transactional
    public List<SkuDetailsPerWarehosue> getSkuDetailPerWarehouse(UUID warehouse_id) {

        return inventoryBalanceService.getSkusPerWarehouse(warehouse_id).stream()
                .map(

                        (obj) -> {

                            String skuCode = (String) (obj[0]);
                            String productName = (String) (obj[1]);
                            BigDecimal quantity = (BigDecimal) (obj[2]);

                            return new SkuDetailsPerWarehosue(skuCode, productName, quantity);

                        }

                ).toList();

    }


}
