package org.springproject.inventorymangaement.records;

import java.util.List;

public record ReservationLookup(String orderReference , List<ItemLog> itemLogs) {
}
