package org.springproject.inventorymangaement.records;

import com.hazelcast.transaction.TransactionOptions;
import org.springproject.inventorymangaement.enums.OrderStatus;
import org.springproject.inventorymangaement.enums.WarehouseType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LedgerAuditLogs(UUID ledger_id , OffsetDateTime timestamp , UUID warehouse_id, String warehouseName , UUID sku_id , String sku_code , String productName , TransactionOptions.TransactionType status, BigDecimal quantity , String details) {
}
