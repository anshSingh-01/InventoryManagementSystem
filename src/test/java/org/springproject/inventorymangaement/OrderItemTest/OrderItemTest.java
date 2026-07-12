package org.springproject.inventorymangaement.OrderItemTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springproject.inventorymangaement.records.Items;
import org.springproject.inventorymangaement.records.OrderReservation;
import org.springproject.inventorymangaement.services.OrderReservationService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class OrderItemTest {

    @Autowired
    OrderReservationService orderReservationService;
    @Test
    public void testingApi(){

        UUID id = (UUID.fromString("f5f0e4aa-698f-413e-9850-2249e45cd365"));
//        orderReservationService.reservingOrder(new OrderReservation("ORD-12983",null, List.of(new Items(id,new BigDecimal(20),new BigDecimal(20)))));

    }

    @Test
    public void testingGetApi(){

        System.out.println(orderReservationService.getReservationLookup("ORD-586770"));

    }


}
