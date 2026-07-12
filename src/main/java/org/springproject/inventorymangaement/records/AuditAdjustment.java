package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.util.UUID;

public record AuditAdjustment(UUID sku_id , UUID user_id, UUID warehouse_id , BigDecimal adjustQuantity , String reason) {
}
