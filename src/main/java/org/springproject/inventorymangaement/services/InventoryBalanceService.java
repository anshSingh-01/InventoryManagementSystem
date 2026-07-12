package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.compositeid.InventoryBalanceId;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.InventoryBalanceDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.WarehouseDto;
import org.springproject.inventorymangaement.entity.InventoryBalance;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.entity.Warehouse;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.InventoryBalanceRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.SkuRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.WarehouseRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryBalanceService implements DtoImpl<InventoryBalance, InventoryBalanceDto> {
    @Autowired
    private InventoryBalanceRepositoryImpl inventoryBalanceRepository;

    @Autowired
    private SkuRepositoryImpl skuRepository;

    @Autowired
    private WarehouseRepositoryImpl warehouseRepository;

    // adding InventoryBalance
    public StatusSender addInventoryBalance(InventoryBalanceDto inventoryBalanceDto) {
        InventoryBalance inventoryBalance = DtoToEntity(inventoryBalanceDto);

        // Validation check to confirm relational bindings resolved successfully
        if (inventoryBalance.getSku() == null || inventoryBalance.getWarehouse() == null) {
            return new StatusSender(StatusCode.ERROR, "Failed to save Inventory Balance: Invalid Sku ID or Warehouse ID references.", inventoryBalanceDto);
        }

        inventoryBalanceRepository.save(inventoryBalance);
        return new StatusSender(StatusCode.SUCCESS, "Added Inventory Balance Successfully", inventoryBalanceDto);
    }

    // add inventory balances
    public StatusSender addInventoryBalances(List<InventoryBalanceDto> inventoryBalanceDtos) {
        List<InventoryBalance> inventoryBalances = inventoryBalanceDtos.stream()
                .map(this::DtoToEntity)
                .filter(balance -> balance.getSku() != null && balance.getWarehouse() != null)
                .toList();

        inventoryBalanceRepository.saveAll(inventoryBalances);
        return new StatusSender(StatusCode.SUCCESS, "Added all valid inventory balances successfully", inventoryBalanceDtos);
    }

    // get inventory balances
    public List<InventoryBalanceDto> getInventoryBalances() {
        List<InventoryBalance> inventoryBalances = inventoryBalanceRepository.findAll();
        return inventoryBalances.stream()
                .map(this::EntityToDto)
                .toList();
    }

    public List<Object[]> getReorderStock(){
                return inventoryBalanceRepository.getAllReorderStock();
    }

    // delete zero avaible quantity

    public void deleteZeroQuantityAvailableRows(){
        inventoryBalanceRepository.deleteZeroQuantityAvailableRows();
    }

    // edit Inventory balance by DTo
    public void editInventoryBalance(InventoryBalanceDto inventoryBalanceDto) {

        UUID sku_id = inventoryBalanceDto.getSkuId();
        UUID warehouse_id = inventoryBalanceDto.getWarehouseId();
        BigDecimal quantityAvailable = inventoryBalanceDto.getQuantityAvailable();
        BigDecimal quanityOnHand = inventoryBalanceDto.getQuantityOnHand();
        InventoryBalance inventoryBalance = getInstanceOfIB(sku_id, warehouse_id);

        Sku sku = skuRepository.findById(sku_id).get();
        Warehouse warehouse = warehouseRepository.findById(warehouse_id).get();

        if (inventoryBalance == null) {

            inventoryBalanceRepository.save(new InventoryBalance(quanityOnHand, quantityAvailable, sku, warehouse));
            return;
        }


        inventoryBalance.setQuantityAvailable(quantityAvailable.add(inventoryBalance.getQuantityAvailable()));
        inventoryBalance.setQuantityOnHand(quanityOnHand.add(inventoryBalance.getQuantityOnHand()));

        inventoryBalanceRepository.save(inventoryBalance);
        return;
    }

    public void editIB(InventoryBalance inventoryBalance){
            inventoryBalanceRepository.save(inventoryBalance);
    }


    public List<Object[]> getSkusPerWarehouse(UUID warehouse_id){
            return inventoryBalanceRepository.getAllSkusInWarehouse(warehouse_id);
    }

    // quantity Available
    public BigDecimal getQuantityAvailable(UUID sku_id, UUID warehouse_id) {
        return inventoryBalanceRepository.findQuantityAvailable(sku_id, warehouse_id);
    }

    // get Instance on the basis of sku and warehouse
    public InventoryBalance getInstanceOfIB(UUID sku_id, UUID warehouse_id) {
        Optional<InventoryBalance> ibOpt = inventoryBalanceRepository.findById(sku_id, warehouse_id);
        if (ibOpt.isPresent()) {
            return ibOpt.get();
        }

        // Construct a new record with 0 quantities if not found
        Sku sku = skuRepository.findById(sku_id)
                .orElseThrow(() -> new RuntimeException("Sku not found: " + sku_id));
        Warehouse wh = warehouseRepository.findById(warehouse_id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found: " + warehouse_id));

        InventoryBalance newIb = new InventoryBalance(BigDecimal.ZERO, BigDecimal.ZERO, sku, wh);
        return inventoryBalanceRepository.save(newIb);
    }

    // quantity on hand
    public BigDecimal getQuantityOnHand(UUID sku_id, UUID warehouse_id) {
        return inventoryBalanceRepository.findQuantityOnHand(sku_id, warehouse_id);
    }

    // get inventory balance by its composite ID components
    public InventoryBalanceDto getInventoryBalanceById(InventoryBalanceId id) {
        return EntityToDto(inventoryBalanceRepository.findById(id).orElse(null));
    }


    public List<Warehouse> findBySkuId(UUID sku_id) {
        return inventoryBalanceRepository.findWarehouseBySkuId(sku_id);
    }

    public long getSkuCount(UUID sku_id) {
        return inventoryBalanceRepository.countSkuBySkuId(sku_id);
    }

    public List<Object[]> getListOfWarehouseBySkuId(UUID sku_id) {
        return inventoryBalanceRepository.grpByWarehouseAndCountSkus(sku_id);
    }

    public void deleteByTwoIds(UUID w_id, UUID sku_id) {
        inventoryBalanceRepository.deleteColumnByWarehouseIdAndSkuId(w_id, sku_id);
    }

    public void deleteUntilRes(Long count, UUID sku_id, UUID w_id) {

        Long total = getSkuCount(sku_id);
        while (count < total) {
            deleteByTwoIds(w_id, sku_id);
            total--;
        }

    }

    @Override
    public InventoryBalance DtoToEntity(InventoryBalanceDto inventoryBalanceDto) {
        // 1. Build the composite primary key class instance from DTO values
        InventoryBalanceId compositeId = new InventoryBalanceId();
        compositeId.setSkuId(inventoryBalanceDto.getSkuId());
        compositeId.setWarehouseId(inventoryBalanceDto.getWarehouseId());

        // 2. Query DB using the composite ID to check for an existing record
        Optional<InventoryBalance> existingBalanceOpt = inventoryBalanceRepository.findById(compositeId);

        InventoryBalance inventoryBalance;
        if (existingBalanceOpt.isPresent()) {
            inventoryBalance = existingBalanceOpt.get();
            inventoryBalance.setQuantityOnHand(inventoryBalanceDto.getQuantityOnHand());
            inventoryBalance.setQuantityAvailable(inventoryBalanceDto.getQuantityAvailable());
        } else {
            inventoryBalance = new InventoryBalance(
                    inventoryBalanceDto.getQuantityOnHand(),
                    inventoryBalanceDto.getQuantityAvailable()
            );
            inventoryBalance.setId(compositeId);
        }

        // 3. Hydrate Sku relational mapping
        if (inventoryBalanceDto.getSkuId() != null) {
            Optional<Sku> skuOpt = skuRepository.findById(inventoryBalanceDto.getSkuId());
            skuOpt.ifPresent(inventoryBalance::setSku);
        }

        // 4. Hydrate Warehouse relational mapping
        if (inventoryBalanceDto.getWarehouseId() != null) {
            Optional<Warehouse> warehouseOpt = warehouseRepository.findById(inventoryBalanceDto.getWarehouseId());
            warehouseOpt.ifPresent(inventoryBalance::setWarehouse);
        }

        return inventoryBalance;
    }


    @Override
    public InventoryBalanceDto EntityToDto(InventoryBalance inventoryBalance) {
        if (inventoryBalance == null) return null;
        InventoryBalanceDto inventoryBalanceDto = new InventoryBalanceDto();
        inventoryBalanceDto.setQuantityOnHand(inventoryBalance.getQuantityOnHand());
        inventoryBalanceDto.setQuantityAvailable(inventoryBalance.getQuantityAvailable());
        // Safe relational mapping extraction from key parts or structural attributes
        if (inventoryBalance.getSku() != null) {
            inventoryBalanceDto.setSkuId(inventoryBalance.getSku().getId());
        } else if (inventoryBalance.getId() != null) {
            inventoryBalanceDto.setSkuId(inventoryBalance.getId().getSkuId());
        }
        if (inventoryBalance.getWarehouse() != null) {
            inventoryBalanceDto.setWarehouseId(inventoryBalance.getWarehouse().getId());
        } else if (inventoryBalance.getSku() != null) {
            inventoryBalanceDto.setWarehouseId(inventoryBalance.getId().getWarehouseId());
        }
        return inventoryBalanceDto;
    }
}