package org.springproject.inventorymangaement.services;

import com.hazelcast.transaction.TransactionOptions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.entity.InventoryAction;
import org.springproject.inventorymangaement.entity.InventoryLedger;
import org.springproject.inventorymangaement.enums.OrderStatus;
import org.springproject.inventorymangaement.enums.TransactionType;
import org.springproject.inventorymangaement.records.AuditUserAdjustment;
import org.springproject.inventorymangaement.records.LedgerAuditLogs;
import org.springproject.inventorymangaement.users.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LedgerService {

    @Autowired
    InventoryLedgerService inventoryLedgerService;

    @Autowired
    InventoryActionService inventoryActionService;

    @Transactional
    public List<LedgerAuditLogs> ledgerService() {

        List<InventoryLedger> inventoryLedgers = inventoryLedgerService.getLedgerEntries()
                .stream().map(inventoryLedgerService::DtoToEntity).toList();

        List<LedgerAuditLogs> ledgerAuditLogs =
                inventoryLedgers.stream()
                        .map(

                                inventoryLedger -> {

                                    // id
                                    UUID ledgerId = inventoryLedger.getId();
                                    // timestamp
                                    OffsetDateTime timestamp = inventoryLedger.getCreatedAt();
                                    // wareshouse id
                                    UUID warehouse_id = inventoryLedger.getWarehouse().getId();
                                    // name
                                    String warehouseName = inventoryLedger.getWarehouse().getName();
                                    // sku_id
                                    UUID sku_id = inventoryLedger.getSku().getId();
                                    // skuName
                                    String skuCode = inventoryLedger.getSku().getSkuCode();
                                    //  productNmae
                                    String productName = inventoryLedger.getSku().getProduct().getName();
                                    // status
                                    TransactionOptions.TransactionType status = inventoryLedger.getTransactionType();
                                    // quantity
                                    BigDecimal quantity = inventoryLedger.getQuantityDelta();

                                    String details = "Inbound PO/Receipt " + inventoryLedger.getReferenceId();

                                    return new LedgerAuditLogs(ledgerId, timestamp, warehouse_id, warehouseName, sku_id, skuCode, productName, status, quantity, details);
                                }

                        ).toList();

        return ledgerAuditLogs;
    }

    @Transactional
    public List<AuditUserAdjustment> ledgerUserActionService(){

                List<InventoryAction> inventoryActions = inventoryActionService.getActions();

                List<AuditUserAdjustment> adjustmentsLogs = inventoryActions.stream()
                        .map(
                                inventoryAction -> {

                                    // Id
                                    UUID id = inventoryAction.getId();
                                    // created at
                                    OffsetDateTime timestamp = inventoryAction.getCreatedAt();

                                    // UserDetails
                                    User user = inventoryAction.getUser();

                                    // skuCode
                                    String skuCode = inventoryAction.getInventoryLedger().getSku().getSkuCode();

                                    String warehouseName=inventoryAction.getInventoryLedger().getWarehouse().getName();

                                    String productName = inventoryAction.getInventoryLedger().getSku().getProduct().getName();

                                    BigDecimal quantity = inventoryAction.getInventoryLedger().getQuantityDelta();

                                    String details = "Inbound PO/Receipt " + inventoryAction.getInventoryLedger().getReferenceId();

                                    return new AuditUserAdjustment(id, timestamp,user.getName(),user.getEmail(),user.getRole(),skuCode ,warehouseName,productName,quantity,details);
                                }
                        ).toList();

            return adjustmentsLogs;
    }


}
