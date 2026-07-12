package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.util.UUID;

public record SkuDetailsPerWarehosue(String skuCode , String ProductName , BigDecimal quantity) {
}
