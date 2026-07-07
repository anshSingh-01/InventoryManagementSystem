package org.springproject.inventorymangaement.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.dtos.SupplierDto;
import org.springproject.inventorymangaement.entity.Supplier;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.SupplierReposotoryImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
public class SupplierService implements DtoImpl<Supplier, SupplierDto> {

        @Autowired
        SupplierReposotoryImpl supplierReposotory;

        // adding Supplier
        public StatusSender addSupplier(SupplierDto supplierDto){
                Supplier supplier = DtoToEntity(supplierDto);
                supplierReposotory.save(supplier);
                return new StatusSender(StatusCode.SUCCESS,"Added Suppliers",supplierDto);
        }

        // add supliers
    public StatusSender addSupliers(List<SupplierDto> supplierDtos){
                List<Supplier> suppliers = supplierDtos.stream()
                        .map(this::DtoToEntity).toList();
                supplierReposotory.saveAll(suppliers);
                return new StatusSender(StatusCode.SUCCESS,"added all supliers",supplierDtos);
    }

    //      get supplier
    public List<SupplierDto> getSuppliers(){
            List<Supplier> suppliers = supplierReposotory.findAll();
            return suppliers.stream()
                    .map(this::EntityToDto).toList();
    }


    // get supplier
    public SupplierDto getSupplierById(UUID id){
                return EntityToDto(supplierReposotory.findById(id).get());
    }

    @Override
    public Supplier DtoToEntity(SupplierDto supplierDto) {
            if (supplierDto.getId() != null) {
                Optional<Supplier> existing = supplierReposotory.findById(supplierDto.getId());
                if (existing.isPresent()) {
                    Supplier s = existing.get();
                    s.setName(supplierDto.getName());
                    s.setContactEmail(supplierDto.getContactEmail());
                    s.setLeadTimeDays(supplierDto.getLeadTimeDays());
                    return s;
                }
            }
            Supplier supplier = supplierReposotory.findByNameAndContactEmail(supplierDto.getName(),supplierDto.getContactEmail());
        return Objects.requireNonNullElseGet(supplier, () -> new Supplier( supplierDto.getName(), supplierDto.getContactEmail(), supplierDto.getLeadTimeDays()));
    }

    @Override
    public SupplierDto EntityToDto(Supplier supplier) {
            if(supplier == null)return null;
            return new SupplierDto( supplier.getName(),supplier.getContactEmail(),supplier.getLeadTimeDays(),supplier.getCreatedAt(),supplier.getUpdatedAt());
    }
}

