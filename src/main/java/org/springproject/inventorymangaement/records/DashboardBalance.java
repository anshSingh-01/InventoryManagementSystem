package org.springproject.inventorymangaement.records;

import org.springproject.inventorymangaement.enums.WarehouseType;

import java.math.BigDecimal;
import java.util.UUID;

public record DashboardBalance(UUID w_id , String warehouseName , WarehouseType type , UUID sku_id, String productName , BigDecimal quantityOnHand , BigDecimal quantityReserved , BigDecimal quantityAvailable) {
}
