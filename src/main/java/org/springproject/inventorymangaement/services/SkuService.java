package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.SkuDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.Product;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.ProductRepositoryImpl;
import org.springproject.inventorymangaement.repositoryimpl.SkuRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SkuService implements DtoImpl<Sku, SkuDto> {

    @Autowired
    private SkuRepositoryImpl skuRepository;

    @Autowired
    private ProductRepositoryImpl productRepository;

    public List<SkuDto> findAllSkus() {
        List<Sku> skus = skuRepository.findAll();
        List<SkuDto> skuDtos = skus.stream()
                .map(sku -> EntityToDto(sku))
                .toList();

        return skuDtos;
    }

    public SkuDto findById(UUID id) {
        Optional<Sku> sku = skuRepository.findById(id);
        if (!sku.isPresent()) return null;
        return EntityToDto(sku.get());
    }

    public StatusSender DeleteById(UUID id) {
        skuRepository.deleteById(id);
        return new StatusSender(StatusCode.SUCCESS, "Successfully Deleted", null);
    }

    public StatusSender addAllSkus(List<SkuDto> skuDtos) {
        List<Sku> skus = skuDtos.stream()
                .map(skuDto -> DtoToEntity(skuDto)).toList();
        skuRepository.saveAll(skus);

        return new StatusSender(StatusCode.SUCCESS, "Saved All Skus", skuDtos);
    }

    public StatusSender addSku(SkuDto skuDto) {
        skuRepository.save(DtoToEntity(skuDto));
        return new StatusSender(StatusCode.SUCCESS, "Sku Saved", skuDto);
    }

    public StatusSender editSku(UUID id, SkuDto skuDto) {
        Sku sku = DtoToEntity(skuDto);
        sku.setId(id); // Ensures we are updating the specific record matching the ID

        skuRepository.save(sku);

        return new StatusSender(StatusCode.SUCCESS, "Sku Edited Successfully!!!", sku);
    }

    @Override
    public Sku DtoToEntity(SkuDto skuDto) {
        // Check if SKU already exists by its unique code
        Sku sku = skuRepository.findBySkuCode(skuDto.getSkuCode());
        if (sku == null) {
            sku = new Sku(skuDto.getSkuCode(), skuDto.getWeight());
        } else {
            sku.setWeight(skuDto.getWeight());
        }
        // Resolve and map the structural ManyToOne Product relationship
        if (skuDto.getProduct_id() != null) {
            Optional<Product> productOpt = productRepository.findById(skuDto.getProduct_id());
            productOpt.ifPresent(sku::setProduct);
        }
        return sku;
    }

    @Override
    public SkuDto EntityToDto(Sku sku) {
        UUID productId = (sku.getProduct() != null) ? sku.getProduct().getId() : null;
        SkuDto dto = new SkuDto(sku.getSkuCode(), productId, sku.getWeight());
        dto.setId(sku.getId());
        return dto;
    }
}
