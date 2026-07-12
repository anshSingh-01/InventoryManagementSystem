package org.springproject.inventorymangaement.entity;

import jakarta.persistence.*;
import org.springproject.inventorymangaement.users.User;

@Entity
@Table(name = "inventory_actions")
public class InventoryAction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    InventoryLedger inventoryLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public InventoryAction() {
        super();
    }

    public InventoryAction(InventoryLedger inventoryLedger, User user) {
        this.inventoryLedger = inventoryLedger;
        this.user = user;
    }

    public InventoryLedger getInventoryLedger() {
        return inventoryLedger;
    }

    public void setInventoryLedger(InventoryLedger inventoryLedger) {
        this.inventoryLedger = inventoryLedger;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
