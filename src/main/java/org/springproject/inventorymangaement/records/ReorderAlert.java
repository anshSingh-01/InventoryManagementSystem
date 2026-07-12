package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.util.UUID;

public record ReorderAlert(UUID sku_id , UUID warehouse_id , BigDecimal quantity) {
}
