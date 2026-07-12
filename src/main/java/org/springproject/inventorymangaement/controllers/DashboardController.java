package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springproject.inventorymangaement.records.AuditUserAdjustment;
import org.springproject.inventorymangaement.records.DashboardBalance;
import org.springproject.inventorymangaement.records.DashboardDetails;
import org.springproject.inventorymangaement.records.LedgerAuditLogs;
import org.springproject.inventorymangaement.services.DashboardService;
import org.springproject.inventorymangaement.services.LedgerService;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {


    @Autowired
    DashboardService dashboardService;

    @Autowired
    LedgerService ledgerService;

    @GetMapping("/details")
    public List<DashboardBalance> getDashboardDetails() {

        return dashboardService.dashboardDetails();

    }

    @GetMapping("/ledgers-list")
    public List<LedgerAuditLogs> getLedgerAuditLogs() {

        return ledgerService.ledgerService();
    }

    @GetMapping("/ledger-user-list")
    public List<AuditUserAdjustment> getAuditUserAdjustment() {

        return ledgerService.ledgerUserActionService();
    }

    @GetMapping("/counts")
    public DashboardDetails getDashboardCount() {
        return dashboardService.getTotalAvailableItems();
    }


}
