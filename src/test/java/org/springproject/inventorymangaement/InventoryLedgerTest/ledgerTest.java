package org.springproject.inventorymangaement.InventoryLedgerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springproject.inventorymangaement.services.LedgerService;

@SpringBootTest
class ledgerTest {

    @Autowired
    LedgerService ledgerService;


    @Test
    void testingLedger() {

            ledgerService.ledgerService()
                    .stream()
                    .forEach(System.out::println);

    }

    @Test

    void testingLedgerUser() {

        ledgerService.ledgerUserActionService()
                .stream()
                .forEach(System.out::println);

    }

}
