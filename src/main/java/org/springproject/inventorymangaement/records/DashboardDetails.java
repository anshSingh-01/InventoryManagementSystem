package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;

public record DashboardDetails(BigDecimal catalogItems , BigDecimal quantityAvailable , BigDecimal quantityOnHand , BigDecimal AvailableSalable) {
}
