package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.util.UUID;

public record Items(UUID sku_id , BigDecimal quantity ,BigDecimal unitPrice) {
}
