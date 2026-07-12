package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;



public record OrderReservation(String orderReference, UUID user_id ,BigDecimal quantity  , List<Items> items) {
}
