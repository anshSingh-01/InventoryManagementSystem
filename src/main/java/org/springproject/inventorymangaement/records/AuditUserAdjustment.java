package org.springproject.inventorymangaement.records;

import org.springframework.context.annotation.Role;
import org.springproject.inventorymangaement.enums.Roles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AuditUserAdjustment(UUID id , OffsetDateTime timestamp , String operatorName , String OperatorEmail , Roles clearance , String skuCode , String warehouseName , String productName, BigDecimal quantity , String details) {
}
