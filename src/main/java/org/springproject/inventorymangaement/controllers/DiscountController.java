package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.DiscountDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.services.DiscountService;


import java.util.List;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @PostMapping("/bulk")
    public StatusSender addDiscounts(@RequestBody List<DiscountDto> discountDtoList) {

        return discountService.addDiscounts(discountDtoList);
    }

    @PostMapping
    public StatusSender addDiscount(@RequestBody DiscountDto discountDto) {

        return discountService.addDiscount(discountDto);
    }

    @GetMapping
    public List<DiscountDto> getDiscounts() {
        return discountService.getAllDiscounts();
    }


}
