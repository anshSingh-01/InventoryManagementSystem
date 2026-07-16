package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;

public record BillItems(String productName , String SkuName, BigDecimal quantity , BigDecimal price,BigDecimal discount) {
}
