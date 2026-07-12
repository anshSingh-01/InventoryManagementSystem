package org.springproject.inventorymangaement.records;

import java.util.UUID;

public record FullfillmentDispatch(String orderReference , UUID warehouse_id , String trackingNumber) {
}
