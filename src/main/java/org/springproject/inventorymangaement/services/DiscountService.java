package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DiscountDto;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.Discount;
import org.springproject.inventorymangaement.entity.Sku;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.DiscountRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@EnableScheduling
@Service
public class DiscountService implements DtoImpl<Discount, DiscountDto> {

    @Autowired
    DiscountRepositoryImpl discountRepository;

    @Autowired
    SkuService skuService;


    Map<UUID, BigDecimal> getDiscountMap() {

        Map<UUID, BigDecimal> map = new HashMap<>();
        List<Object[]> list = discountRepository.getDiscountDetails();

        for (Object[] obj : list) {

            map.put((UUID) obj[0], (BigDecimal) obj[1]);

        }
        return map;
    }

    public StatusSender addDiscounts(List<DiscountDto> discountDtoList) {
        discountRepository.saveAll(discountDtoList.stream().map(this::DtoToEntity).toList());
        return new StatusSender(StatusCode.SUCCESS, "Successfull entered all discounts", discountDtoList);
    }

    public StatusSender addDiscount(DiscountDto discountDto) {
        discountRepository.save(DtoToEntity(discountDto));
        return new StatusSender(StatusCode.SUCCESS, "Successfull entered all discounts", discountDto);
    }

    public List<DiscountDto> getAllDiscounts() {

        return discountRepository.findAll().stream().map(this::EntityToDto).toList();

    }


    @Scheduled(cron = "0 0 0 * * *")
    void deleteInactiveDiscounts() {
        discountRepository.deleteInactiveDiscounts();
    }


@Scheduled(cron = "0 0 * * * *") // every hour
public void updateDiscountStatus() {
    LocalDate now = LocalDate.now();
    List<Discount> discounts = discountRepository.findAll();
    for (Discount d : discounts) {
        boolean shouldBeActive = !now.isBefore(d.getStartTime()) && !now.isAfter(d.getEndTime());
        if (d.isActive() != shouldBeActive) {
            d.setActive(shouldBeActive);
            discountRepository.save(d);
        }
    }
}

    @Override
    public Discount DtoToEntity(DiscountDto discountDto) {


        UUID sku_id = discountDto.getSku_id();
        Discount discountE = discountRepository.getDiscountBySkuId(sku_id);

        if (discountE == null) {
            discountE = new Discount();
        }

        Sku sku = skuService.findById(sku_id);
        BigDecimal discount = discountDto.getDiscount();
        LocalDate startDate = discountDto.getStartDate();
        LocalDate endDate = discountDto.getEndDate();
        boolean active = discountDto.isActive();

        discountE.setSku(sku);
        discountE.setDiscount(discount);
        discountE.setStartTime(startDate);
        discountE.setEndTime(endDate);
        discountE.setActive(active);
        return discountE;
    }

    @Override
    public DiscountDto EntityToDto(Discount discount) {
        return new DiscountDto(discount.getSku().getId(),discount.getDiscount(),discount.getStartTime(),discount.getEndTime(),discount.isActive());
    }
}
