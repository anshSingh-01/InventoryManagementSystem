package org.springproject.inventorymangaement.dtos;

import java.util.UUID;

public class InventoryActionDto {

    private UUID log_id;
    private UUID user_id;

    public InventoryActionDto(){}

    public InventoryActionDto(UUID log_id, UUID user_id) {
        this.log_id = log_id;
        this.user_id = user_id;
    }

    public UUID getLog_id() {
        return log_id;
    }

    public void setLog_id(UUID log_id) {
        this.log_id = log_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }
}
