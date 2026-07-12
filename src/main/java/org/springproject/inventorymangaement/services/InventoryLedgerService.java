package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.InventoryLedgerDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.InventoryLedger;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.entity.Warehouse;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.InventoryLedgerRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.SkuRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.WarehouseRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryLedgerService implements DtoImpl<InventoryLedger, InventoryLedgerDto> {

    @Autowired
    private InventoryLedgerRepositoryImpl inventoryLedgerRepository;

    @Autowired
    private SkuRepositoryImpl skuRepository;

    @Autowired
    private WarehouseRepositoryImpl warehouseRepository;

    // adding InventoryLedger
    public StatusSender addLedgerEntry(InventoryLedgerDto inventoryLedgerDto) {
        InventoryLedger ledger = DtoToEntity(inventoryLedgerDto);

        // Fail-fast evaluation if relationships are unresolvable
        if (ledger.getSku() == null || ledger.getWarehouse() == null) {
            return new StatusSender(StatusCode.ERROR, "Failed to save Ledger Entry: Invalid Sku ID or Warehouse ID references.", inventoryLedgerDto);
        }

        ledger = inventoryLedgerRepository.save(ledger);
        return new StatusSender(StatusCode.SUCCESS, "Added Ledger Entry Successfully", ledger);
    }

    // add ledger entries
    public StatusSender addLedgerEntries(List<InventoryLedgerDto> inventoryLedgerDtos) {
        List<InventoryLedger> ledgers = inventoryLedgerDtos.stream()
                .map(this::DtoToEntity)
                .filter(ledger -> ledger.getSku() != null && ledger.getWarehouse() != null)
                .toList();

        List<InventoryLedger> saved = inventoryLedgerRepository.saveAll(ledgers);
        List<InventoryLedgerDto> savedDtos = saved.stream().map(this::EntityToDto).toList();
        return new StatusSender(StatusCode.SUCCESS, "Added all valid ledger entries successfully", savedDtos);
    }

    // get ledger entries
    public List<InventoryLedgerDto> getLedgerEntries() {
        List<InventoryLedger> ledgers = inventoryLedgerRepository.findAll();
        return ledgers.stream()
                .map(this::EntityToDto)
                .toList();
    }

    // get ledger entry by ID
    public InventoryLedgerDto getLedgerEntryById(UUID id) {
        return EntityToDto(inventoryLedgerRepository.findById(id).orElse(null));
    }

    // get ledger Id

    public UUID getLedgerIdBySkuIDAndWareHouseId(UUID sku_id , UUID warehouse_id){

            InventoryLedger inventoryLedger = inventoryLedgerRepository.findBYTwoIds(sku_id,warehouse_id);
            return inventoryLedger.getId();
    }

    public InventoryLedger getLedgerEntityById(UUID id){
            return inventoryLedgerRepository.findById(id).orElse(null);
    }


    // getting list of ledger with OrderReference

    public List<InventoryLedger> getListOfLedgers(String orderReference){

            return inventoryLedgerRepository.findInventoryLedgerByOrderReference(orderReference);

    }


    @Override
    public InventoryLedger DtoToEntity(InventoryLedgerDto inventoryLedgerDto) {
        // Because ledgers represent an immutable historical timeline audit trail,
        // we directly initialize a fresh record instead of updating an existing one.
        InventoryLedger ledger = new InventoryLedger(
                inventoryLedgerDto.getTransactionType(),
                inventoryLedgerDto.getQuantityDelta(),
                inventoryLedgerDto.getReferenceId(),
                inventoryLedgerDto.getCreatedAt()
        );

        if (inventoryLedgerDto.getId() != null) {
            ledger.setId(inventoryLedgerDto.getId());
        }

        // 1. Hydrate full Sku Entity from DB using the DTO's UUID
        if (inventoryLedgerDto.getSkuId() != null) {
            Optional<Sku> skuOpt = skuRepository.findById(inventoryLedgerDto.getSkuId());
            skuOpt.ifPresent(ledger::setSku);
        }

        // 2. Hydrate full Warehouse Entity from DB using the DTO's UUID
        if (inventoryLedgerDto.getWarehouseId() != null) {
            Optional<Warehouse> warehouseOpt = warehouseRepository.findById(inventoryLedgerDto.getWarehouseId());
            warehouseOpt.ifPresent(ledger::setWarehouse);
        }

        return ledger;
    }

    @Override
    public InventoryLedgerDto EntityToDto(InventoryLedger inventoryLedger) {
        if (inventoryLedger == null) return null;

        InventoryLedgerDto dto = new InventoryLedgerDto();
        dto.setId(inventoryLedger.getId());
        dto.setTransactionType(inventoryLedger.getTransactionType());
        dto.setQuantityDelta(inventoryLedger.getQuantityDelta());
        dto.setReferenceId(inventoryLedger.getReferenceId());
        dto.setCreatedAt(inventoryLedger.getCreatedAt());

        // Extract raw database entity UUIDs safely for the client DTO mappings
        if (inventoryLedger.getSku() != null) {
            dto.setSkuId(inventoryLedger.getSku().getId());
        }
        if (inventoryLedger.getWarehouse() != null) {
            dto.setWarehouseId(inventoryLedger.getWarehouse().getId());
        }

        return dto;
    }
}