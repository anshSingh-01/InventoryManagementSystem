package org.springproject.inventorymangaement.records;

import java.util.List;
import java.util.UUID;

public record StockReorderDetails(UUID warehouse_id ,UUID supplier_id  , String reference_id , List<Items> items) {
}
