package org.springproject.inventorymangaement.ReorderRuleTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springproject.inventorymangaement.services.OrderReservationService;
import org.springproject.inventorymangaement.services.StockReceivingService;

import java.util.UUID;

@SpringBootTest
class ReoderApiTest {

    @Autowired
    OrderReservationService orderReservationService;

    @Autowired
    StockReceivingService stockReceivingService;

    @Test
    void reorderTest() {

        orderReservationService.getAllReorderAlert().stream()
                .forEach(System.out::println);


    }

    @Test
    void reorderTest1() {

        stockReceivingService.getSkuDetailPerWarehouse(UUID.fromString("31d5019a-93c7-4d49-adf4-6ad913d02916")).stream()
                .forEach(System.out::println);


    }
}
