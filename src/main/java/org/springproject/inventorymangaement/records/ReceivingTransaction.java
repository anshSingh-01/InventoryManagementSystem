package org.springproject.inventorymangaement.records;

import java.util.List;
import java.util.UUID;

public record ReceivingTransaction(UUID warehouse_id , UUID supplier_id , String refrence_id , List<Items> items) {
}
