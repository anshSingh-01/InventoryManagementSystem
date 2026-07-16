package org.springproject.inventorymangaement.OrderItemTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springproject.inventorymangaement.records.BillRecord;
import org.springproject.inventorymangaement.services.DiscountService;
import org.springproject.inventorymangaement.services.OrderItemService;
import org.springproject.inventorymangaement.services.OrderReservationService;

import java.util.UUID;

@SpringBootTest
public class OrderItemTest {

    @Autowired
    OrderReservationService orderReservationService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    DiscountService discountService;
    @Test
    public void testingApi(){

        UUID id = (UUID.fromString("f5f0e4aa-698f-413e-9850-2249e45cd365"));
//        orderReservationService.reservingOrder(new OrderReservation("ORD-12983",null, List.of(new Items(id,new BigDecimal(20),new BigDecimal(20)))));

    }

    @Test
    public void testingGetApi(){

        System.out.println(orderReservationService.getReservationLookup("ORD-766664"));

    }

    @Test
    public void testingDiscountApi(){

        discountService.getAllDiscounts().stream().forEach(

                (discountDto)->{
                            discountDto.setActive(false);
                            discountService.addDiscount(discountDto);
                }

        );

    }




    @Test
    public void ChangingUnitPrice(){

//        List<OrderItem> orderItems = orderItemService.getOrderItems().stream().map(orderItemService::DtoToEntity).toList();
//
//        orderItems.stream().forEach(
//                orderItem -> {
//                    Random random = new Random();
//                    BigDecimal price = new BigDecimal(random.nextDouble(9000) + 1000);
//                    orderItem.setUnitPrice(price);
//
//                }
//
//        );
//
//       orderItemService.saveAllOrderItems(orderItems);


        BillRecord billRecord =orderReservationService.generateBill("ORD-766664");

        System.out.println(billRecord.orderReference() + " " + billRecord.totalBill());
        billRecord.billItems().stream().forEach(System.out::println);


    }


}
