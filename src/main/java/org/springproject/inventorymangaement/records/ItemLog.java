package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemLog(UUID sku_id , String skuCode , String productName , BigDecimal quantity) {
}
