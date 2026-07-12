package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.util.UUID;

public record RelocationTransaction(UUID user_id ,UUID sku_id , UUID source , UUID destination , BigDecimal quantity) {
}
