package org.springproject.inventorymangaement.InventoryBalanceTest;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springproject.inventorymangaement.records.DashboardBalance;
import org.springproject.inventorymangaement.services.DashboardService;

import java.util.List;

@SpringBootTest
class DashBoardTest {

    @Autowired
    DashboardService dashboardService;
    @Test

    void testingOfDashboarService() {

        dashboardService.dashboardDetails().stream()
                .forEach(System.out::println);

    }
}
