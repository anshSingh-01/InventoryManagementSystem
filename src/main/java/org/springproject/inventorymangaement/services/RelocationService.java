package org.springproject.inventorymangaement.services;


import com.hazelcast.transaction.TransactionOptions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.InventoryActionDto;
import org.springproject.inventorymangaement.dtos.InventoryLedgerDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.InventoryBalance;
import org.springproject.inventorymangaement.entity.InventoryLedger;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.records.RelocationTransaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RelocationService {

    @Autowired
    InventoryBalanceService inventoryBalanceService;

    @Autowired
    InventoryLedgerService inventoryLedgerService;
    @Autowired
    InventoryActionService inventoryActionService;

    @Transactional
    public StatusSender relocatingItems(RelocationTransaction relocationTransaction){
        UUID user_id = relocationTransaction.user_id();
        UUID sku_id  = relocationTransaction.sku_id();
        UUID source = relocationTransaction.source();
        UUID destination = relocationTransaction.destination();
        BigDecimal quantity = relocationTransaction.quantity();

        InventoryBalance inventoryBalance1 = inventoryBalanceService.getInstanceOfIB(sku_id,source);
        InventoryBalance inventoryBalance2 = inventoryBalanceService.getInstanceOfIB(sku_id,destination);

        if(inventoryBalance1.getQuantityOnHand().intValue()  < quantity.intValue()){
                    return new StatusSender(StatusCode.ERROR ,"Insufficent Items in Source Warehouse",null);
        }

        inventoryBalance1.setQuantityOnHand(inventoryBalance1.getQuantityOnHand().subtract(quantity));
        inventoryBalance1.setQuantityAvailable(inventoryBalance1.getQuantityAvailable().subtract(quantity));
        StatusSender statusSender =inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id,source, TransactionOptions.TransactionType.TWO_PHASE,quantity,"item deducted from "+ inventoryBalance1.getWarehouse().getName() + "{RELEASED}", OffsetDateTime.now()));

        InventoryLedger inventoryLedger = (InventoryLedger)statusSender.getObj();
        inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), user_id));

        inventoryBalanceService.editIB(inventoryBalance1);

        if(inventoryBalance2 != null) {
            inventoryBalance2.setQuantityOnHand(inventoryBalance2.getQuantityOnHand().add(quantity));
            inventoryBalance2.setQuantityAvailable(inventoryBalance2.getQuantityAvailable().add(quantity));

        }
        else{
            inventoryBalance2 = new InventoryBalance(quantity,quantity,inventoryBalance1.getSku(),inventoryBalance1.getWarehouse());
        }
       statusSender = inventoryLedgerService.addLedgerEntry(new InventoryLedgerDto(sku_id,destination, TransactionOptions.TransactionType.TWO_PHASE,quantity,"item received from" + inventoryBalance2.getWarehouse().getName() + "{RECIEVED}", OffsetDateTime.now()));
         inventoryLedger = (InventoryLedger)statusSender.getObj();
        inventoryActionService.addAction(new InventoryActionDto(inventoryLedger.getId(), user_id));
        inventoryBalanceService.editIB(inventoryBalance2);

        return new StatusSender(StatusCode.SUCCESS,"Transfer Successfull",inventoryBalance2);
    }


}
