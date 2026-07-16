package org.springproject.inventorymangaement.records;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record BillRecord(String orderReference , List<BillItems> billItems , BigDecimal totalBill , OffsetDateTime issuedAt) {
}
