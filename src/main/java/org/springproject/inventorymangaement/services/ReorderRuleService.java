package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.ReorderRuleDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.ReorderRule;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.entity.Warehouse;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.ReorderRuleRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.SkuRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.WarehouseRepositoryImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReorderRuleService implements DtoImpl<ReorderRule, ReorderRuleDto> {

    @Autowired
    private ReorderRuleRepositoryImpl reorderRuleRepository;

    @Autowired
    private SkuRepositoryImpl skuRepository;

    @Autowired
    private WarehouseRepositoryImpl warehouseRepository;

    // adding ReorderRule
    public StatusSender addReorderRule(ReorderRuleDto reorderRuleDto) {
        ReorderRule reorderRule = DtoToEntity(reorderRuleDto);
        reorderRuleRepository.save(reorderRule);
        return new StatusSender(StatusCode.SUCCESS, "Added Reorder Rule", reorderRuleDto);
    }

    // add reorder rules
    public StatusSender addReorderRules(List<ReorderRuleDto> reorderRuleDtos) {
        List<ReorderRule> reorderRules = reorderRuleDtos.stream()
                .map(this::DtoToEntity)
                .toList();
        reorderRuleRepository.saveAll(reorderRules);
        return new StatusSender(StatusCode.SUCCESS, "Added all reorder rules", reorderRuleDtos);
    }

    // get reorder rules
    public List<ReorderRuleDto> getReorderRules() {
        List<ReorderRule> reorderRules = reorderRuleRepository.findAll();
        return reorderRules.stream()
                .map(this::EntityToDto)
                .toList();
    }

    // get reorder rule
    public ReorderRuleDto getReorderRuleById(UUID id) {
        return EntityToDto(reorderRuleRepository.findById(id).orElse(null));
    }




    @Override
    public ReorderRule DtoToEntity(ReorderRuleDto reorderRuleDto) {
        // Enforcing the unique constraint requirement: one mapping unique combination per sku + warehouse
        ReorderRule reorderRule = reorderRuleRepository.findBySkuIdAndWarehouseId(
                reorderRuleDto.getSkuId(),
                reorderRuleDto.getWarehouseId()
        );

        ReorderRule finalizedRule = Objects.requireNonNullElseGet(reorderRule, () -> new ReorderRule(
                reorderRuleDto.getReorderThreshold(),
                reorderRuleDto.getTargetReorderQuantity(),
                reorderRuleDto.getCreatedAt(),
                reorderRuleDto.getUpdatedAt()
        ));

        // If it was fetched from the DB, update thresholds manually
        if (reorderRule != null) {
            finalizedRule.setReorderThreshold(reorderRuleDto.getReorderThreshold());
            finalizedRule.setTargetReorderQuantity(reorderRuleDto.getTargetReorderQuantity());
            finalizedRule.setUpdatedAt(reorderRuleDto.getUpdatedAt());
        }

        // Hydrate Sku relational mapping
        if (reorderRuleDto.getSkuId() != null) {
            Optional<Sku> skuOpt = skuRepository.findById(reorderRuleDto.getSkuId());
            skuOpt.ifPresent(finalizedRule::setSku);
        }

        // Hydrate Warehouse relational mapping
        if (reorderRuleDto.getWarehouseId() != null) {
            Optional<Warehouse> warehouseOpt = warehouseRepository.findById(reorderRuleDto.getWarehouseId());
            warehouseOpt.ifPresent(finalizedRule::setWarehouse);
        }

        return finalizedRule;
    }

    @Override
    public ReorderRuleDto EntityToDto(ReorderRule reorderRule) {
        if (reorderRule == null) return null;

        ReorderRuleDto reorderRuleDto = new ReorderRuleDto();

        // Map direct fields
        reorderRuleDto.setReorderThreshold(reorderRule.getReorderThreshold());
        reorderRuleDto.setTargetReorderQuantity(reorderRule.getTargetReorderQuantity());
        reorderRuleDto.setCreatedAt(reorderRule.getCreatedAt());
        reorderRuleDto.setUpdatedAt(reorderRule.getUpdatedAt());

        // Map relational FK references safely avoiding NullPointerExceptions
        if (reorderRule.getSku() != null) {
            reorderRuleDto.setSkuId(reorderRule.getSku().getId());
        }
        if (reorderRule.getWarehouse() != null) {
            reorderRuleDto.setWarehouseId(reorderRule.getWarehouse().getId());
        }

        return reorderRuleDto;
    }
}