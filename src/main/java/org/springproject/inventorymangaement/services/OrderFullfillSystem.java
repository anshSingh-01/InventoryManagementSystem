package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderFullfillSystem implements CommandLineRunner {



    @Autowired
    OrderReservationService orderReservationService;

    @Override
    public void run(String... args) throws Exception {
        orderReservationService.startFullfillingOrder();
    }
}
