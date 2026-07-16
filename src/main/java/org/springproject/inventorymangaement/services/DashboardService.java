package org.springproject.inventorymangaement.services;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.entity.InventoryBalance;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.entity.Warehouse;
import org.springproject.inventorymangaement.enums.WarehouseType;
import org.springproject.inventorymangaement.records.DashboardBalance;
import org.springproject.inventorymangaement.records.DashboardDetails;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class DashboardService {

    @Autowired
    InventoryBalanceService inventoryBalanceService;

    @Autowired
    ProductService productService;

    @Transactional
    public List<DashboardBalance> dashboardDetails() {

        List<InventoryBalance> inventoryBalances = inventoryBalanceService.getInventoryBalances().stream().map(inventoryBalanceService::DtoToEntity).toList();

        List<DashboardBalance> dashboardBalances = inventoryBalances.stream().map(
                inventoryBalance -> {
                    UUID w_id = inventoryBalance.getWarehouse().getId();
                    String warehouseName = inventoryBalance.getWarehouse().getName();
                    WarehouseType type = inventoryBalance.getWarehouse().getType();

                    Sku sku = inventoryBalance.getSku();
                    Warehouse warehouse = inventoryBalance.getWarehouse();
                    String productName = sku.getProduct().getName();
                    BigDecimal quantityAvailable = inventoryBalanceService.getQuantityAvailable(sku.getId(), warehouse.getId());
                    BigDecimal quantityOnHand = inventoryBalanceService.getQuantityOnHand(sku.getId(), warehouse.getId());
                    BigDecimal reservedQuantity = quantityOnHand.subtract(quantityAvailable);
                    return new DashboardBalance(w_id, warehouseName, type, sku.getId(), productName, quantityAvailable, reservedQuantity, quantityOnHand);

                }
        ).toList();

        return dashboardBalances;
    }


    @Transactional
    public DashboardDetails getTotalAvailableItems() {

        int available = inventoryBalanceService.getInventoryBalances().stream()
                .map(inventoryBalanceService::DtoToEntity)
                .mapToInt(
                        (inventoryBalance) -> inventoryBalance.getQuantityAvailable().intValue()
                ).sum();

        int onHand = inventoryBalanceService.getInventoryBalances().stream()
                .map(inventoryBalanceService::DtoToEntity)
                .mapToInt(
                        (inventoryBalance) -> inventoryBalance.getQuantityOnHand().intValue()
                ).sum();

        long catalogCount = productService.findAllProducts().stream().count();

        int salaibleItems = onHand - available;

        return new DashboardDetails(new BigDecimal(catalogCount), new BigDecimal(available), new BigDecimal(onHand), new BigDecimal(salaibleItems));

    }


}
