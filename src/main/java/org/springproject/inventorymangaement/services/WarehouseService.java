package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.WarehouseDto;
import org.springproject.inventorymangaement.entity.Warehouse;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.WarehouseRepositoryImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseService implements DtoImpl<Warehouse, WarehouseDto> {

    @Autowired
    private WarehouseRepositoryImpl warehouseRepository;

    // adding Warehouse
    public StatusSender addWarehouse(WarehouseDto warehouseDto) {
        Warehouse warehouse = DtoToEntity(warehouseDto);
        warehouseRepository.save(warehouse);
        return new StatusSender(StatusCode.SUCCESS, "Added Warehouse", warehouseDto);
    }

    // add warehouses
    public StatusSender addWarehouses(List<WarehouseDto> warehouseDtos) {
        List<Warehouse> warehouses = warehouseDtos.stream()
                .map(this::DtoToEntity)
                .toList();
        warehouseRepository.saveAll(warehouses);
        return new StatusSender(StatusCode.SUCCESS, "Added all warehouses", warehouseDtos);
    }

    // get warehouses
    public List<WarehouseDto> getWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        return warehouses.stream()
                .map(this::EntityToDto)
                .toList();
    }

    // get warehouse
    public WarehouseDto getWarehouseById(UUID id) {
        return EntityToDto(warehouseRepository.findById(id).orElse(null));
    }

    @Override
    public Warehouse DtoToEntity(WarehouseDto warehouseDto) {
        if (warehouseDto.getId() != null) {
            Optional<Warehouse> existing = warehouseRepository.findById(warehouseDto.getId());
            if (existing.isPresent()) {
                Warehouse w = existing.get();
                w.setName(warehouseDto.getName());
                w.setType(warehouseDto.getType());
                w.setAddress(warehouseDto.getAddress());
                return w;
            }
        }
        Warehouse warehouse = warehouseRepository.findByNameAndAddress(warehouseDto.getName(), warehouseDto.getAddress());
        return Objects.requireNonNullElseGet(warehouse, () -> new Warehouse(
                warehouseDto.getName(),
                warehouseDto.getType(),
                warehouseDto.getAddress(),
                warehouseDto.getCreatedAt(),
                warehouseDto.getUpdatedAt()
        ));
    }

    @Override
    public WarehouseDto EntityToDto(Warehouse warehouse) {
        if (warehouse == null) return null;

        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setId(warehouse.getId());
        warehouseDto.setName(warehouse.getName());
        warehouseDto.setType(warehouse.getType());
        warehouseDto.setAddress(warehouse.getAddress());
        warehouseDto.setCreatedAt(warehouse.getCreatedAt());
        warehouseDto.setUpdatedAt(warehouse.getUpdatedAt());

        return warehouseDto;
    }
}